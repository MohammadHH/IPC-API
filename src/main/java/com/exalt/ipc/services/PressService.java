package com.exalt.ipc.services;

import com.exalt.ipc.entities.IPC;
import com.exalt.ipc.entities.Job;
import com.exalt.ipc.entities.Press;
import com.exalt.ipc.entities.User;
import com.exalt.ipc.exception.CommonExceptions;
import com.exalt.ipc.repositories.PressRepository;
import com.exalt.ipc.requests.PressRequest;
import com.exalt.ipc.responses.MappedPressInfoResponse;
import com.exalt.ipc.responses.PressResponse;
import com.exalt.ipc.utilities.Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.exalt.ipc.utilities.Constants.HELD_QUEUE_SIZE_LIMIT;
import static com.exalt.ipc.utilities.Constants.Printing_QUEUE_LIMIT;

@Service
public class PressService {

	//Store mapped presses
	private static final Map<Integer, PressQueueService> pressMap = new HashMap();

	public static final String PRESS_QUEUE_SERVICE = "pressQueueService";

	@Autowired
	private PressRepository pressRepository;

	@Autowired
	private JobService jobService;

	@Autowired
	private JwtService jwtService;

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
	public Press addPress(PressRequest pressRequest) {
		Press savedPress = pressRepository.save(Dto.from(pressRequest));
		//Create and save press queues
		heldQueueService.save(savedPress, HELD_QUEUE_SIZE_LIMIT);
		printingQueueService.save(savedPress, Printing_QUEUE_LIMIT);
		retainedQueueService.save(savedPress);
		//Get a new pressQueueService for the given press [prototype bean]
		PressQueueService pressQueueService = applicationContext.getBean(PRESS_QUEUE_SERVICE, PressQueueService.class);
		pressQueueService.setUpPress(savedPress);
		//Add the press to the map
		pressMap.put(savedPress.getId(), pressQueueService);
		return savedPress;
	}

	public PressResponse getPressInfo(int pressId) {
		Press press = getPressThrowIfNotFound(pressId);
		return Dto.from(press, isMapped(press));
	}

	//Return a press if it has a valid id
	public Press getPressThrowIfNotFound(int pressId) {
		return getOptionalPress(pressId).orElseThrow(() -> CommonExceptions.PRESS_NOT_FOUND_EXCEPTION);
	}

	@Transactional
	public PressResponse updatePress(int pressId, PressRequest pressRequest) {
		Press press = getPressThrowIfNotFound(pressId);
		press.setName(pressRequest.getName());
		press.setAddress(pressRequest.getAddress());
		press.setDescription(pressRequest.getDescription());
		return Dto.from(press, isMapped(press));
	}

	public Press getPress(int pressId) {
		return getOptionalPress(pressId).get();
	}

	public Optional<Press> getOptionalPress(int pressId) {
		return pressRepository.findById(pressId);
	}

	public boolean isMapped(Press press) {
		return ipcService.getIPCByPressId(press.getId()).isPresent();
	}

	//Map the press if the press is unmapped and has valid id
	public PressResponse map(int pressId, String jwt) {
		return map(getPressThrowIfNotFound(pressId), userService.getUserByJwt(jwt));
	}

	@Transactional
	//Map the press if the press is unmapped
	public PressResponse map(Press press, User user) {
		if (isMapped(press))
			throw CommonExceptions.PRESS_ALREADY_MAPPED_EXCEPTION;
		//Set the pressId to the ipc [map it in the database]
		IPC ipc = ipcService.getIPC(user.getId());
		ipc.setPress(press);
		ipcService.saveIPC(ipc);
		return Dto.from(pressRepository.save(press), isMapped(press));
	}

	public MappedPressInfoResponse getMappedPressInfo(String jwt) {
		Press press = findMappedPress(jwt);
		if (press == null)
			throw CommonExceptions.NO_MAPPED_PRESS_EXCEPTION2;
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

	public List<Job> moveTo(int pressId, List<Job> jobs, String dst) {
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
		canPrint(1, press);
		return pressMap.get(press.getId()).printFront();
	}

	public List<Job> printAll(String jwt) {
		Press press = findMappedPress(jwt);
		canPrint(pressMap.get(press.getId()).numberOfJobsInHeldQueue(), press);
		return pressMap.get(press.getId()).printAll();
	}

	public void canPrint(int jobNumbers, Press press) {
		if (press == null)
			throw CommonExceptions.NO_MAPPED_PRESS_EXCEPTION;
		PressQueueService pressQueueService = pressMap.get(press.getId());
		if (!pressQueueService.printingQueueCanHandle(jobNumbers))
			throw CommonExceptions.NO_ROOM_IN_PRINTING_QUEUE_EXCEPTION;
		if (pressQueueService.isEmptyHeldQueue())
			throw CommonExceptions.NO_JOB_TO_PRINT_EXCEPTION;
	}


	//First unmap then delete
	public void deletePress(int pressId, String jwt) {
		Press press = getPressThrowIfNotFound(pressId);
		if (isMapped(press))
			unmap(pressId);
		deletePress(press);
	}

	//Unmap a mapped press
	public PressResponse unmap(int pressId) {
		Press press = getPressThrowIfNotFound(pressId);
		if (!isMapped(press))
			throw CommonExceptions.PRESS_ALREADY_UNMAPPED_EXCEPTION;
		//Delete the pressQueueService attached with the mapped press
		PressQueueService pressQueueService = pressMap.get(press.getId());
		pressQueueService.stopPrinting();
		//restore the jobs from the press queues to the ipc
		pressQueueService.restoreJobsToIpc();
		//pressMap.remove(pressId);
		//unmap the press in DB [by nulling the IPC pressId]
		IPC ipc = ipcService.getIPCByPressId(press.getId()).get();
		ipc.setPress(null);
		ipcService.saveIPC(ipc);
		pressRepository.save(press);
		return Dto.from(press, isMapped(press));
	}

	public void deletePress(Press press) {
		//delete press's attached queues then delete press
		heldQueueService.delete(press);
		printingQueueService.delete(press);
		retainedQueueService.delete(press);
		pressRepository.deleteById(press.getId());
	}
}
