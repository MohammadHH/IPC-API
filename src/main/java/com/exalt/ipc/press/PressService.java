package com.exalt.ipc.press;

import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.helpers.HelperSerivce;
import com.exalt.ipc.ipc.IPCRepository;
import com.exalt.ipc.job.File;
import com.exalt.ipc.job.FileRepository;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.press.queues.*;
import com.exalt.ipc.user.User;
import com.exalt.ipc.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.exalt.ipc.job.States.HELDING;
import static com.exalt.ipc.job.States.UPLOADED;

@Service
public class PressService {
    @Autowired
    private PressRepository pressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IPCRepository ipcRepository;
    @Autowired
    private HeldQueueRepository heldQueueRepository;
    @Autowired
    private PrintingQueueRepository printingQueueRepository;
    @Autowired
    private ReturnedQueueRepository returnedQueueRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private LocaleService localeService;
    @Autowired
    private HelperSerivce helperSerivce;
    private static Map<Integer, PressQueueService> pressMap = new HashMap();


    @Transactional
    public Press storePress(User user, String description) {
        Press press = new Press(user, description);
        press.setIpc(user.getIpc());
        press.setMapped(false);
        Press savedPress = pressRepository.save(press);
        PressQueue pressQueue = new PressQueue();
        pressQueue.setPress(savedPress);
        heldQueueRepository.save(new HeldQueue(pressQueue, 150_000_000));
        printingQueueRepository.save(new PrintingQueue(pressQueue, 20));
        returnedQueueRepository.save(new ReturnedQueue(pressQueue));
        return savedPress;
    }

    @Transactional
    public Press map(Press press, HttpServletRequest request) {
        if (pressRepository.findByUserEmailAndMappedTrue(press.getUser().getEmail()).isPresent())
            throw new CustomException("7050", request, HttpStatus.BAD_REQUEST, localeService, 7051);
        PressQueueService pressQueueService = new PressQueueService(fileRepository);
        pressQueueService.setHeldQueueSizeLimit(heldQueueRepository.findByPressId(press.getId()).get().getSizeLimit());
        pressQueueService.setPrintingQueueItemsLimit(printingQueueRepository.findByPressId(press.getId()).get().getItemsLimit());
        pressMap.put(press.getId(), pressQueueService);
        System.out.println("pressMap");
        pressMap.forEach((k, v) -> System.out.println(k + ": " + v));
        press.setMapped(true);
        return pressRepository.save(press);
    }


    @Transactional
    public Press unmap(Press press, HttpServletRequest request) {
        System.out.println("pressMap");
        pressMap.forEach((k, v) -> System.out.println(k + ": " + v));
        PressQueueService pressQueueService = pressMap.get(press.getId());
        if (pressQueueService.isEmptyPrintingQueue())
            throw new CustomException("7080", request, HttpStatus.BAD_REQUEST, localeService, 7081);
        pressMap.remove(press.getId());
        press.setMapped(false);
        return pressRepository.save(press);
    }

    @Transactional
    public File addJob(Press press, File job, HttpServletRequest request) {
        CustomException ex;
        List<Integer> errors = new LinkedList<>();
        if (!job.getState().equals(UPLOADED))
            errors.add(7061);
        if (!press.isMapped())
            errors.add(7062);
        if (!errors.isEmpty())
            throw new CustomException("7060", request, HttpStatus.BAD_REQUEST, localeService, errors);
        PressQueueService pressQueueService = pressMap.get(press.getId());
        pressQueueService.addToHeldQueue(job, request);
        job.setState(HELDING);
        System.out.println("PressQueueService is: ");
        System.out.println(pressQueueService);
        return fileRepository.save(job);
    }

    @Transactional
    public List<File> addJobs(Press press, List<File> jobs, HttpServletRequest request) {
        PressQueueService pressQueueService = pressMap.get(press.getId());
        if (!pressQueueService.heldQueueHasEnoughSize(jobs))
            throw new CustomException("7060", request, HttpStatus.BAD_REQUEST, localeService, 7060, 7063);
        List<File> addedJobs = new LinkedList<>();
        jobs.forEach((job) -> addedJobs.add(addJob(press, job, request)));
        return addedJobs;
    }

    public File printJob(Press press, HttpServletRequest request) {
        CustomException ex;
        if (!press.isMapped())
            throw new CustomException("7070", request, HttpStatus.BAD_REQUEST, localeService, 7071);
        PressQueueService pressQueueService = pressMap.get(press.getId());
        if (pressQueueService.isHeldQueueEmpty())
            throw new CustomException("7070", request, HttpStatus.BAD_REQUEST, localeService, 7072);
        if (pressQueueService.printingQueueResidualUnits() == 0)
            throw new CustomException("7070", request, HttpStatus.BAD_REQUEST, localeService, 7073);
        return pressMap.get(press.getId()).printFront();
    }

    public List<File> printAllJobs(Press press, HttpServletRequest request) {
        CustomException ex;
        if (!press.isMapped())
            throw new CustomException("7070", request, HttpStatus.BAD_REQUEST, localeService, 7071);
        PressQueueService pressQueueService = pressMap.get(press.getId());
        if (pressQueueService.isHeldQueueEmpty())
            throw new CustomException("7070", request, HttpStatus.BAD_REQUEST, localeService, 7072);
        if (pressQueueService.printingQueueResidualUnits() < pressQueueService.heldQueueItemsSize())
            throw new CustomException("7070", request, HttpStatus.BAD_REQUEST, localeService, 7074);
        List<File> files = new LinkedList<>();
        int size = pressQueueService.heldQueueItemsSize();
        for (int i = 0; i < size; i++)
            files.add(pressMap.get(press.getId()).printFront());
        return files;
    }

    public Press getPress(int pressId, HttpServletRequest request) {
        CustomException ex = new CustomException("7030", request, HttpStatus.NOT_FOUND, localeService, 7031);
        Press press = pressRepository.findById(pressId).orElseThrow(() -> ex);
        return press;
    }

    public Map<String, Object> getPressInfo(Press press, HttpServletRequest request) {
        PressQueueService pressQueueService = pressMap.get(press.getId());
        Map<String, Object> map = new LinkedHashMap();
        map.put("id", press.getId());
        map.put("description", press.getDescription());
        map.put("sizeLimit", (pressQueueService.getHeldQueueSizeLimit() / 1_000_000) + " MB");
        map.put("residualSize", (pressQueueService.heldQueueResidualSize() / 1_000_000) + " MB");
        map.put("heldJobs", pressQueueService.heldQueueItemsSize());
        map.put("itemsLimit", pressQueueService.getPrintingQueueItemsLimit());
        map.put("jobsInProgress", pressQueueService.printingQueueItemsSize());
        map.put("expectedJobCompletionTime", TimeUnit.MILLISECONDS.toSeconds(pressQueueService.printingQueueEstimatedCompletionTime()) + " sec");
        map.put("finishedJobs", pressQueueService.totalFinishedJobs());
        return map;
    }
}
