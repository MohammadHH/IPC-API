package com.exalt.ipc.services;

import com.exalt.ipc.entities.IPC;
import com.exalt.ipc.entities.*;
import com.exalt.ipc.repositories.HeldQueueRepository;
import com.exalt.ipc.repositories.PrintingQueueRepository;
import com.exalt.ipc.repositories.RetainedQueueRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.exalt.ipc.utilities.Constants.HELD_QUEUE_SIZE_LIMIT;
import static com.exalt.ipc.utilities.Constants.Printing_QUEUE_LIMIT;
import static com.exalt.ipc.utilities.States.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PressQueueServiceTest {
	Logger logger = LoggerFactory.getLogger(PressQueueServiceTest.class);

	public static final int FILE_SIZE = 5_000_0;

	public static final int PRINTING_INTERVAL = FILE_SIZE / 1000;

	public static final int SLEEP_INTERVAL = 100;

	private static PressQueueService pressQueueService;

	@Mock
	private JobService jobService = mock(JobService.class);

	@Mock
	private HeldQueueRepository heldQueueRepository = mock(HeldQueueRepository.class);

	@Mock
	private PrintingQueueRepository printingQueueRepository = mock(PrintingQueueRepository.class);

	@Mock
	private RetainedQueueRepository retainedQueueRepository = mock(RetainedQueueRepository.class);

	private static final User user = new User();

	private static final Press press = new Press("testPress", "192.168.2.1", "printing press");


	private static final File file = new File("testFile", "application/pdf", FILE_SIZE);

	private List<Job> jobs;

	@BeforeAll
	public static void setup() {
		user.setEmail("mohammad@gmail.com");
		user.setId(1);
		IPC ipc = new IPC();
		ipc.setUser(user);
		user.setIpc(ipc);
	}

	@BeforeEach
	public void setupQueueJobs() {
		when(heldQueueRepository.findByPressId(anyInt()))
				.thenReturn(Optional.of(new HeldQueue(press, HELD_QUEUE_SIZE_LIMIT)));
		when(printingQueueRepository.findByPressId(anyInt()))
				.thenReturn(Optional.of(new PrintingQueue(press, Printing_QUEUE_LIMIT)));
		jobs = Arrays.asList(getDummyJob(), getDummyJob(), getDummyJob(), getDummyJob(), getDummyJob());
		for (int i = 0; i < jobs.size(); i++)
			when(jobService.saveJob(jobs.get(i))).thenReturn(jobs.get(i));
	}

	Job getDummyJob() {
		return new Job(UPLOADED, file, user);
	}


	@Test
	public void testMovingJobsBetweenQueues() throws InterruptedException {
		pressQueueService =
				new PressQueueService(heldQueueRepository, printingQueueRepository, retainedQueueRepository, jobService);
		pressQueueService.setUpPress(press);
		Queue<Job> heldQueue = new LinkedList<>(jobs);
		pressQueueService.setHeldQueue(heldQueue);
		logger.info("Jobs before movement to printing queue {} ", jobs);
		pressQueueService.moveJobsBetweenPressQueues(jobs, HELDING, PRINTING);
		logger.info("Jobs after movement to printing queue {} ", jobs);
		assertThat(jobs).allMatch((job) -> job.getState().equals(PRINTING), "all should be in state of printing");
		Thread.sleep(jobs.size() * (PRINTING_INTERVAL + SLEEP_INTERVAL));
		logger.info("Jobs after printing had been finished {} ", jobs);
		assertThat(jobs).allMatch((job) -> job.getState().equals(RETAINED), "all should be printed and in retained queue");
	}

}
