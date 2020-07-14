package com.exalt.ipc.press.queues;

import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.job.File;
import com.exalt.ipc.job.FileRepository;
import com.exalt.ipc.localization.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.exalt.ipc.job.States.PRINTING;
import static com.exalt.ipc.job.States.RETURNED;

@Service
public class PressQueueService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private LocaleService localeService;
    private Queue<File> heldQueue = new LinkedList<>();
    private Queue<File> printingQueue = new LinkedList<>();
    private Queue<File> returnedQueue = new LinkedList<>();
    private long heldQueueSizeLimit = 0;
    private int printingQueueItemsLimit = 0;
    private Thread printingWork;

    public PressQueueService() {
    }

    public PressQueueService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        heldQueue = new LinkedList<>();
        printingQueue = new LinkedList<>();
        returnedQueue = new LinkedList<>();
        printingWork = new Thread(() -> {
            Thread.currentThread().setName("prnitingThread");
            System.out.println("inside prnitingThread");
            System.out.println("printingQueue=" + printingQueue);

            while (true) {
                if (!printingQueue.isEmpty())
                    print();
                else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        printingWork.start();
    }

    public boolean addToHeldQueue(File file, HttpServletRequest request) {
        if (totalSize(heldQueue) + file.getSize() > heldQueueSizeLimit)
            throw new CustomException("7060", request, HttpStatus.BAD_REQUEST, localeService, 7062);
        heldQueue.add(file);
        return true;

    }

    public int heldQueueItemsSize() {
        return heldQueue.size();
    }

    public boolean heldQueueHasEnoughSize(List<File> files) {
        return totalSize(files) + totalSize(heldQueue) <= heldQueueSizeLimit;
    }

    public long totalSize(Collection<File> files) {
        return files.stream().reduce(0L, (subtotal, file) -> subtotal + file.getSize(), Long::sum);
    }

    private boolean print() {
        if (printingQueue.isEmpty()) return false;
        System.out.println("inside print with thread " + Thread.currentThread().getName());
        Long size = printingQueue.element().getSize() / 1_000_000;
        try {
            Thread.sleep(size * 1000L);
        } catch (InterruptedException e) {
            System.out.println("print() exception");
        }
        System.out.println("line64" + printingQueue);
        File file = printingQueue.remove();
        System.out.println("finished printing " + file);
        file.setState(RETURNED);
        fileRepository.save(file);
        returnedQueue.add(file);
        return true;
    }


    public boolean removeFromHeldQueue() {
        heldQueue.remove();
        return true;
    }

    public int totalFinishedJobs() {
        return returnedQueue.size();
    }

    public File printFront() {
        File file = heldQueue.remove();
        file.setState(PRINTING);
        fileRepository.save(file);
        printingQueue.add(file);
        return file;
    }

    public int printingQueueResidualUnits() {
        return printingQueueItemsLimit - printingQueue.size();
    }

    public long heldQueueResidualSize() {
        return heldQueueSizeLimit - totalSize(heldQueue);
    }

    public long printingQueueEstimatedCompletionTime() {
        return totalSize(printingQueue) / 1_000_000 * 1000L;
    }

    public int printingQueueItemsSize() {
        return printingQueue.size();
    }

    public boolean isHeldQueueEmpty() {
        return heldQueue.isEmpty();
    }

    public boolean printAll() {
        if ((printingQueue.size() + heldQueue.size()) > printingQueueItemsLimit)
            return false;
        heldQueue.stream().forEach(file -> printFront());
        return true;
    }

    public boolean addToReturnedQueue(File file) {
        return returnedQueue.add(file);
    }

    public boolean isEmptyPrintingQueue() {
        return printingQueue.isEmpty();
    }

    public boolean removeFromReturnedQueue() {
        return true;
    }

    public long getHeldQueueSizeLimit() {
        return heldQueueSizeLimit;
    }

    public void setHeldQueueSizeLimit(long heldQueueSizeLimit) {
        this.heldQueueSizeLimit = heldQueueSizeLimit;
    }

    public int getPrintingQueueItemsLimit() {
        return printingQueueItemsLimit;
    }

    public void setPrintingQueueItemsLimit(int printingQueueItemsLimit) {
        this.printingQueueItemsLimit = printingQueueItemsLimit;
    }


    @Override
    public String toString() {
        return "PressQueueService{" +
                "heldQueue=" + heldQueue +
                ", printingQueue=" + printingQueue +
                ", returnedQueue=" + returnedQueue +
                '}';
    }
}
