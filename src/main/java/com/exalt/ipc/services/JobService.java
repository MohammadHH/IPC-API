package com.exalt.ipc.services;

import com.exalt.ipc.entities.File;
import com.exalt.ipc.entities.Job;
import com.exalt.ipc.entities.User;
import com.exalt.ipc.exception.CommonExceptions;
import com.exalt.ipc.repositories.JobRepository;
import com.exalt.ipc.utilities.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.exalt.ipc.utilities.States.UPLOADED;
import static com.exalt.ipc.utilities.States.getStates;

@Service
public class JobService {
	public static final String[] mimes = {"application/pdf", "image/jpeg", "image/png"};

	public static final String E7030 = "7030";

	@Autowired
	PressService pressService;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private IPCService ipcService;

	@Autowired
	private UserService userService;

	@Transactional
	public List<Job> uploadJobs(String jwt, MultipartFile[] files) {
		User user = userService.getUserByJwt(jwt);
		validateFiles(files);
		List<Job> savedJobs = new ArrayList<>();
		if (files.length == 0 || files == null)
			throw CommonExceptions.EMPTY_UPLOADED_FILE_EXCEPTION;
		if (ipcService.getResidual(jwt) < files.length)
			throw CommonExceptions.NO_ROOM_IN_IPC_QUEUE_EXCEPTION;
		Arrays.stream(files).forEach(file -> {
			savedJobs.add(uploadJob(user, file));
		});
		return savedJobs;
	}

	public void validateFiles(MultipartFile[] files) {
		Arrays.stream(files).forEach((file) -> validateFile(file));
	}

	@Transactional
	private Job uploadJob(User user, MultipartFile file) {
		File uploadedFile = fileStorageService.storeFile(file);
		return saveJob(new Job(UPLOADED, uploadedFile, user));
	}

	public void validateFile(MultipartFile file) {
		if (file.isEmpty())
			throw CommonExceptions.EMPTY_UPLOADED_FILE_EXCEPTION;
		if (!Arrays.stream(mimes).anyMatch(mime -> file.getContentType().equals(mime)))
			throw CommonExceptions.INVALID_FILE_MIME_EXCEPTION;
		//throw new HttpMediaTypeNotSupportedException(MediaType.valueOf(file.getContentType()), Collections.emptyList(),"")
	}

	public Job saveJob(Job job) {
		return jobRepository.save(job);
	}

	public void deleteJobsByIDs(List<Integer> IDs, String jwt) {
		deleteJobs(getJobsForUser(IDs, userService.getUserByJwt(jwt).getId()), jwt);
	}

	public void deleteJobs(List<Job> jobs, String jwt) {
		List<Job> queued = jobs.stream().filter(job -> !job.getState().equals(UPLOADED)).collect(Collectors.toList());
		pressService.deleteJobsFromPress(queued, jwt);
		jobs.forEach(job -> deleteJob(job));
	}

	public List<Job> getJobsForUser(List<Integer> IDs, int userId) {
		return IDs.stream().collect(Collectors
				.mapping(id -> getOptionalJobByUserId(id, userId).orElseThrow(() -> CommonExceptions.JOB_NOT_FOUND_EXCEPTION),
						Collectors.toList()));
	}

	public Boolean deleteJob(Job job) {
		jobRepository.deleteById(job.getId());
		return true;
	}

	public Optional<Job> getOptionalJob(int id) {
		return jobRepository.findById(id);
	}

	public Optional<Job> getOptionalJobByUserId(int id, int userId) {
		return jobRepository.findByIdAndUserId(id, userId);
	}

	public List<Job> getUploaded(String jwt) {
		return jobRepository.findByStateAndUserId(UPLOADED, userService.getUserByJwt(jwt).getId());
	}

	public List<Job> getAllJobs(String jwt) {
		return getAllJobs(userService.getUserByJwt(jwt));
	}

	public List<Job> getAllJobs(User user) {
		return jobRepository.findByUserId(user.getId());
	}

	public List<Job> getJobs(String state, String jwt) {
		validState(state);
		return jobRepository.findByStateAndUserId(state, userService.getUserByJwt(jwt).getId());
	}

	private void validState(String state) {
		if (!Arrays.stream(getStates()).anyMatch((s) -> s.equals(state)))
			throw CommonExceptions.INVALID_JOB_STATE_EXCEPTION;
	}

	public void deleteUserJobs(User user) {
		List<Job> jobs = getAllJobs(user);
		jobs.stream().forEach(jobRepository::delete);
	}

	public List<Job> moveJobs(int pressId, List<Integer> IDs, String src, String dst, String jwt) {
		String srcState = States.get(src), dstState = States.get(dst);
		//If the press is not mapped, don't move jobs
		if (pressService.findMappedPress(jwt) == null)
			throw CommonExceptions.PRESS_ALREADY_UNMAPPED_EXCEPTION;
		if (srcState == null)
			throw CommonExceptions.INVALID_JOB_SOURCE_EXCEPTION;
		if (dstState == null)
			throw CommonExceptions.INVALID_JOB_DESTINATION_EXCEPTION;
		if (srcState.equals(UPLOADED))
			return pressService.moveTo(pressId, getJobsForUser(IDs, userService.getUserByJwt(jwt).getId()), dstState);
		else
			return pressService
					.moveBetween(pressId, getJobsForUser(IDs, userService.getUserByJwt(jwt).getId()), srcState, dstState);
	}
}
