package com.exalt.ipc.services;

import com.exalt.ipc.entities.IPC;
import com.exalt.ipc.entities.Job;
import com.exalt.ipc.entities.Press;
import com.exalt.ipc.entities.User;
import com.exalt.ipc.repositories.PressRepository;
import com.exalt.ipc.requests.PressRequest;
import com.exalt.ipc.responses.MappedPressInfoResponse;
import com.exalt.ipc.responses.PressResponse;
import com.exalt.ipc.utilities.Dto;
import com.exalt.ipc.utilities.HelperSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.exalt.ipc.utilities.Constants.HELD_QUEUE_SIZE_LIMIT;
import static com.exalt.ipc.utilities.Constants.Printing_QUEUE_LIMIT;

@Service
public class PressService {
	private static final Map<Integer, PressQueueService> pressMap = new HashMap();

	@Autowired
	private PressRepository pressRepository;

	@Autowired
	private JobService jobService;

	@Autowired
	private HelperSerivce helperSerivce;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private HeldQueueService heldQueueService;

	@Autowired
	private PrintingQueueService printingQueueService;

	@Autowired
	private RetainedQueueService retainedQueueService;

	@Autowired
	private UserService userService;

	@Autowired
	private IPCService ipcService;


	@Transactional
	public Press storePress(PressRequest pressRequest) {
		Press savedPress = pressRepository.save(Dto.from(pressRequest));
		heldQueueService.save(savedPress, HELD_QUEUE_SIZE_LIMIT);
		printingQueueService.save(savedPress, Printing_QUEUE_LIMIT);
		retainedQueueService.save(savedPress);
		return savedPress;
	}

	@Transactional
	public PressResponse updatePress(int pressId, PressRequest pressRequest) {
		Press press = getPress(pressId);
		press.setName(pressRequest.getName());
		press.setAddress(pressRequest.getAddress());
		press.setDescription(pressRequest.getDescription());
		return Dto.from(press, isMapped(press));
	}

	public Press getPress(int pressId) {
		//		CustomException ex = new CustomException("7030", HttpStatus.NOT_FOUND, 7031);
		return pressRepository.findById(pressId).get();
		//													.orElseThrow(() -> null);
	}

	public boolean isMapped(Press press) {
		return ipcService.getIPCByPressId(press.getId()).isPresent();
	}

	@Transactional
	public Press map(int pressId, String jwt) {
		return map(getPress(pressId), userService.getUserByJwt(jwt));
	}

	@Transactional
	public Press map(Press press, User user) {
		//		if (press.getIpc() != null) {
		//			throw new CustomException("7050", HttpStatus.BAD_REQUEST, 7051);
		//		}
		PressQueueService pressQueueService = applicationContext.getBean("pressQueueService", PressQueueService.class);
		pressQueueService.setPressAndQueues(press);
		pressMap.put(press.getId(), pressQueueService);
		System.out.println("pressMap");
		pressMap.forEach((k, v) -> System.out.println(k + ": " + v));
		IPC ipc = ipcService.getIPC(user.getId());
		ipc.setPress(press);
		ipcService.saveIPC(ipc);
		//		press.setIpc(user.getIpc());
		return pressRepository.save(press);
	}

	@Transactional
	public Press unmap(Press press) {
		System.out.println("pressMap");
		pressMap.forEach((k, v) -> System.out.println(k + ": " + v));
		PressQueueService pressQueueService = pressMap.get(press.getId());
		if (pressQueueService.isEmptyPrintingQueue()) {
			//			throw new CustomException("7080", HttpStatus.BAD_REQUEST, 7081);
		}
		pressMap.remove(press.getId());
		//		press.setIpc(null);
		IPC ipc = ipcService.getIPCByPressId(press.getId()).get();
		ipc.setPress(null);
		ipcService.saveIPC(ipc);
		return pressRepository.save(press);
	}

	public MappedPressInfoResponse getMappedPressInfo(String jwt) {
		Press press = findMappedPress(jwt);
		PressQueueService pressQueueService = pressMap.get(press.getId());
		MappedPressInfoResponse response = new MappedPressInfoResponse();
		response.setId(press.getId());
		response.setName(press.getName());
		response.setAddress(press.getAddress());
		response.setDescription(press.getDescription());
		response.setCreationDate(press.getCreationDate());
		response.setSizeLimit((pressQueueService.getHeldQueueLimit() / 1_000_000) + " MB");
		response.setResidualSize((pressQueueService.heldQueueResidualSize() / 1_000_000) + " MB");
		response.setHeldJobs(pressQueueService.heldQueueItemsSize());
		response.setItemsLimit(pressQueueService.getPrintingQueueLimit());
		response.setJobsInProgress(pressQueueService.printingQueueItemsSize());
		response.setExpectedPrintingCompletionTime(
				TimeUnit.MILLISECONDS.toSeconds(pressQueueService.printingQueueEstimatedCompletionTime()) + " sec");
		response.setFinishedJobs(pressQueueService.totalFinishedJobs());
		return response;
	}

	public Press findMappedPress(String jwt) {
		return ipcService.getIPC(userService.getUserByJwt(jwt).getId()).getPress();

	}

	public PressResponse getPressInfo(int pressId) {
		Press press = getPress(pressId);
		return Dto.from(press, isMapped(press));
	}

	public List<Job> moveTo(int pressId, List<Job> jobs, String dst) {
		System.out.println("PressService -> pressId= " + pressId + ",job= " + jobs.stream() + ",dst= " + dst);
		return pressMap.get(pressId).addToQueue(dst, jobs);
	}

	public List<Job> moveBetween(int pressId, List<Job> jobs, String oldState, String newState) {
		System.out.println("PressService -> pressId= " + pressId + ",job= " + jobs.stream());
		return pressMap.get(pressId).moveJobsBetweenPressQueues(jobs, oldState, newState);
	}

	public void deleteJobsFromPress(List<Job> jobs, String jwt) {
		Press mappedPress = findMappedPress(jwt);
		if (mappedPress != null)
			pressMap.get(mappedPress.getId()).deleteJobs(jobs);
	}

	public Job printFront(String jwt) {
		Press press = findMappedPress(jwt);
		return pressMap.get(press.getId()).printFront();
	}

	public List<Job> printAll(String jwt) {
		Press press = findMappedPress(jwt);
		return pressMap.get(press.getId()).printAll();
	}

	public void deletePress(int pressId, String jwt) {
		Press press = getPress(pressId);
		if (isMapped(press))
			unmap(pressId);
		System.out.println("after unmap and before deletePress");
		deletePress(press);
	}

	public PressResponse unmap(int pressId) {
		Press press = getPress(pressId);
		PressQueueService pressQueueService = pressMap.get(press.getId());
		pressQueueService.stopPrinting();
		pressQueueService.restoreJobsToIpc();
		pressMap.remove(pressId);
		IPC ipc = ipcService.getIPCByPressId(press.getId()).get();
		ipc.setPress(null);
		//		press.setIpc(null);
		ipcService.saveIPC(ipc);
		pressRepository.save(press);
		return Dto.from(press, isMapped(press));
	}

	public void deletePress(Press press) {
		heldQueueService.delete(press);
		printingQueueService.delete(press);
		retainedQueueService.delete(press);
		System.out.println("gonna delete the press " + press);
		pressRepository.deleteById(press.getId());
	}
}
