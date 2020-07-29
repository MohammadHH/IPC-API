package com.exalt.ipc.services;

import com.exalt.ipc.entities.Job;
import com.exalt.ipc.entities.Press;
import com.exalt.ipc.repositories.HeldQueueRepository;
import com.exalt.ipc.repositories.PrintingQueueRepository;
import com.exalt.ipc.repositories.RetainedQueueRepository;
import com.exalt.ipc.utilities.PrintingThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.exalt.ipc.utilities.States.*;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PressQueueService {

	public static final int SLEEP_INTERVAL = 100;

	@Autowired
	private HeldQueueRepository heldQueueRepository;

	@Autowired
	private PrintingQueueRepository printingQueueRepository;

	@Autowired
	private RetainedQueueRepository retainedQueueRepository;

	@Autowired
	private JobService jobService;

	public PressQueueService() {
	}

	public PressQueueService(HeldQueueRepository heldQueueRepository, PrintingQueueRepository printingQueueRepository,
			RetainedQueueRepository retainedQueueRepository, JobService jobService) {
		this.heldQueueRepository = heldQueueRepository;
		this.printingQueueRepository = printingQueueRepository;
		this.retainedQueueRepository = retainedQueueRepository;
		this.jobService = jobService;
	}

	private Press press;

	private Queue<Job> heldQueue = new LinkedList<>();

	private Queue<Job> printingQueue = new LinkedList<>();

	private Queue<Job> retainedQueue = new LinkedList<>();

	private long heldQueueLimit = 0;

	private int printingQueueLimit = 0;

	private AtomicBoolean stopPrinting = new AtomicBoolean(false);

	private PrintingThread printingWork = new PrintingThread(SLEEP_INTERVAL, () -> print());

	public void setHeldQueue(Queue<Job> heldQueue) {
		this.heldQueue = heldQueue;
	}

	public void setPrintingQueue(Queue<Job> printingQueue) {
		this.printingQueue = printingQueue;
	}

	public void setRetainedQueue(Queue<Job> retainedQueue) {
		this.retainedQueue = retainedQueue;
	}

	public void startPrinting() {
		if (printingWork.isStopped())
			printingWork.start();
	}

	public void setUpPress(Press press) {
		this.press = press;
		setHeldQueueLimit(getHeldQueueSizeLimit(press));
		setPrintingQueueLimit(getPrintingQueueItemsLimit(press));
		startPrinting();
	}

	public void setHeldQueueLimit(long heldQueueLimit) {
		this.heldQueueLimit = heldQueueLimit;
	}

	public void setPrintingQueueLimit(int printingQueueLimit) {
		this.printingQueueLimit = printingQueueLimit;
	}

	public long getHeldQueueSizeLimit(Press press) {
		return heldQueueRepository.findByPressId(press.getId()).get().getSizeLimit();
	}

	public int getPrintingQueueItemsLimit(Press press) {
		return printingQueueRepository.findByPressId(press.getId()).get().getItemsLimit();
	}

	public List<Job> moveJobsBetweenPressQueues(List<Job> jobs, String oldState, String newState) {
		stopPrinting.set(true);
		List<Job> movedJobs = jobs.stream().collect(
				Collectors.mapping(job -> moveJobBetweenPressQueues(job, oldState, newState), Collectors.toList()));
		stopPrinting.set(false);
		return movedJobs;
	}

	private Job moveJobBetweenPressQueues(Job job, String oldState, String newState) {
		job.setState(newState);
		getQueue(newState).add(deleteFromQueue(oldState, job));
		return jobService.saveJob(job);
	}

	public Queue<Job> getQueue(String queue) {
		switch (queue) {
			case HELDING:
				return heldQueue;
			case PRINTING:
				return printingQueue;
			case RETAINED:
				return retainedQueue;
			default:
				return null;
		}
	}

	public Job deleteFromQueue(String queue, Job jobToDelete) {
		getQueue(queue).removeIf(job -> job.getId() == jobToDelete.getId());
		return jobToDelete;
	}

	public void deleteJobs(List<Job> jobs) {
		stopPrinting.set(true);
		jobs.forEach(job -> deleteFromQueue(job.getState(), job));
		stopPrinting.set(false);
	}

	private int print() {
		//don't do anything if there is no job to print
		if (printingQueue.isEmpty())
			return 0;
		Long size = printingQueue.element().getFile().getSize() / 1_000_000;
		try {
			//simulate printing time for the current job
			Thread.sleep(size * 1000L);
			//if press has been stopped don't continue
			if (stopPrinting.get()) {
				stopPrinting.set(false);
				return -1;
			}
		} catch (InterruptedException e) {
		}
		Job job = printingQueue.remove();
		job.setState(RETAINED);
		jobService.saveJob(job);
		retainedQueue.add(job);
		return 1;
	}

	public void restoreJobsToIpc() {
		stopPrinting.set(true);
		restoreFromQueueToIpc(HELDING);
		restoreFromQueueToIpc(PRINTING);
		restoreFromQueueToIpc(RETAINED);
	}

	public void restoreFromQueueToIpc(String queue) {
		Queue<Job> q = getQueue(queue);
		q.forEach(job -> moveToIpc(job));
		q = new LinkedList<>();
	}

	public void moveToIpc(Job job) {
		job.setState(UPLOADED);
		jobService.saveJob(job);
	}


	public int heldQueueItemsSize() {
		return heldQueue.size();
	}


	public int totalFinishedJobs() {
		return retainedQueue.size();
	}

	public long heldQueueResidualSize() {
		return getHeldQueueLimit() - totalSize(heldQueue);
	}

	public long getHeldQueueLimit() {
		return heldQueueLimit;
	}

	public long totalSize(Collection<Job> jobs) {
		return jobs.stream().reduce(0L, (subtotal, job) -> subtotal + job.getFile().getSize(), Long::sum);
	}

	public long printingQueueEstimatedCompletionTime() {
		return totalSize(printingQueue) / 1_000_000 * 1000L;
	}

	public int printingQueueItemsSize() {
		return printingQueue.size();
	}

	public int numberOfJobsInHeldQueue() {
		return heldQueue.size();
	}

	public List<Job> printAll() {
		List<Job> added = addToQueue(PRINTING, heldQueue.stream().collect(Collectors.toList()));
		heldQueue = new LinkedList<>();
		return added;
	}

	public List<Job> addToQueue(String queue, List<Job> jobs) {
		if (printingWork.isRunning())
			printingWork.stop();
		List<Job> addedJobs = jobs.stream().collect(Collectors.mapping(job -> addToQueue(queue, job), Collectors.toList()));
		if (printingWork.isStopped())
			printingWork.start();
		return addedJobs;
	}

	public Job addToQueue(String queue, Job job) {
		job.setState(queue);
		getQueue(queue).add(job);
		Job savedJob = jobService.saveJob(job);
		return savedJob;
	}

	public Job printFront() {
		return addToQueue(PRINTING, Arrays.asList(heldQueue.remove())).get(0);
	}


	public boolean isEmptyPrintingQueue() {
		return printingQueue.isEmpty();
	}

	public boolean isEmptyHeldQueue() {
		return heldQueue.isEmpty();
	}

	public boolean printingQueueCanHandle(int jobNumbers) {
		return (printingQueueLimit - printingQueueItemsSize()) > jobNumbers;
	}

	public int getPrintingQueueLimit() {
		return printingQueueLimit;
	}

	public void stopPrinting() {
		if (printingWork.isRunning()) {
			System.out.println("stopPrinting() is called");
			printingWork.stop();
		}
	}

}
