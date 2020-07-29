package com.exalt.ipc.services;

import com.exalt.ipc.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {JobService.class})
public class JobServiceTest {
	@Autowired
	private JobService jobService;

	@MockBean
	PressService pressService;

	@MockBean
	private JobRepository jobRepository;

	@MockBean
	private FileStorageService fileStorageService;

	@MockBean
	private IPCService ipcService;

	@MockBean
	private UserService userService;

}
