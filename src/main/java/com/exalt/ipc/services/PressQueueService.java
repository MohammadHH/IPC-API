package com.exalt.ipc.services;

import com.exalt.ipc.entities.HeldQueue;
import com.exalt.ipc.entities.Job;
import com.exalt.ipc.entities.Press;
import com.exalt.ipc.entities.PrintingQueue;
import com.exalt.ipc.repositories.HeldQueueRepository;
import com.exalt.ipc.repositories.PrintingQueueRepository;
import com.exalt.ipc.repositories.RetainedQueueRepository;
import com.exalt.ipc.utilities.PrintingThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.exalt.ipc.utilities.States.*;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PressQueueService {

	@Autowired
	private HeldQueueRepository heldQueueRepository;

	@Autowired
	private PrintingQueueRepository printingQueueRepository;

	@Autowired
	private RetainedQueueRepository retainedQueueRepository;

	@Autowired
	private JobService jobService;

	private Press press;

	private Queue<Job> heldQueue = new LinkedList<>();

	private Queue<Job> printingQueue = new LinkedList<>();

	private Queue<Job> retainedQueue = new LinkedList<>();

	private long heldQueueLimit = 0;

	private int printingQueueLimit = 0;

	private PrintingThread printingWork = new PrintingThread(1000, () -> {
		print();
		return 0;
	});

	public Press getPress() {
		return press;
	}

	public void setPress(Press press) {
		this.press = press;
	}

	public void setPressAndQueues(Press press) {
		setPress(press);
		this.heldQueueLimit = getHeldQueue(press).getSizeLimit();
		this.printingQueueLimit = getPrintingQueue(press).getItemsLimit();
	}

	public HeldQueue getHeldQueue(Press press) {
		return heldQueueRepository.findByPressId(press.getId()).get();
	}

	public PrintingQueue getPrintingQueue(Press press) {
		return printingQueueRepository.findByPressId(press.getId()).get();
	}

	public List<Job> moveJobsBetweenPressQueues(List<Job> jobs, String oldState, String newState) {
		if (printingWork.isRunning())
			printingWork.stop();
		List<Job> movedJobs = jobs.stream().collect(
				Collectors.mapping(job -> moveJobBetweenPressQueues(job, oldState, newState), Collectors.toList()));
		if (printingWork.isStopped())
			printingWork.start();
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
		System.out.println("PressQueueService.class deleteFrom " + queue + " queue " + jobToDelete.getId());
		return jobToDelete;
	}

	public void deleteJobs(List<Job> jobs) {
		if (printingWork.isRunning())
			printingWork.stop();
		jobs.forEach(job -> deleteFromQueue(job.getState(), job));
		if (printingWork.isStopped())
			printingWork.start();
	}

	public Job deleteQueueFront(String queue) {
		Queue<Job> q = getQueue(queue);
		if (q != null)
			return q.remove();
		else
			return null;
		//FIXME
	}

	private boolean print() {
		if (printingQueue.isEmpty()) {
			System.out.println("Stopping the printing thread because the queue is empty");
			printingWork.stop();
			return false;
		}
		Long size = printingQueue.element().getFile().getSize() / 1_000_000;
		try {
			Thread.sleep(size * 1000L);
		} catch (InterruptedException e) {
		}
		Job job = printingQueue.remove();
		job.setState(RETAINED);
		jobService.saveJob(job);
		retainedQueue.add(job);
		return true;
	}

	public void restoreJobsToIpc() {
		if (printingWork.isRunning())
			printingWork.stop();
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

	//@formatter:on

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

	public List<Job> printAll() {
		List<Job> added = addToQueue(PRINTING, heldQueue.stream().collect(Collectors.toList()));
		heldQueue = new LinkedList<>();
		return added;
	}

	//@formatter:off
	public List<Job> addToQueue(String queue,List<Job> jobs){
		if(printingWork.isRunning())printingWork.stop();
		List<Job> addedJobs=jobs.stream().collect(Collectors.mapping(job -> addToQueue(queue,job), Collectors.toList()));
		if(printingWork.isStopped())printingWork.start();
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

	public int getPrintingQueueLimit() {
		return printingQueueLimit;
	}
	public void stopPrinting(){
		if (printingWork.isRunning()){
		System.out.println("stopPrinting() is called");
			printingWork.stop();
		}
	}

}
