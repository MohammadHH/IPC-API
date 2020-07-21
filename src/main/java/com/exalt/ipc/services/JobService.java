package com.exalt.ipc.services;

import com.exalt.ipc.entities.File;
import com.exalt.ipc.entities.Job;
import com.exalt.ipc.entities.User;
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
		if (ipcService.getResidual(jwt) >= files.length) {
			Arrays.stream(files).forEach(file -> {
				try {
					savedJobs.add(uploadJob(user, file));
				} catch (Exception e) {
				}
			});
		} else {
			//			throw new CustomException("7010", HttpStatus.BAD_REQUEST, 7011);
		}
		return savedJobs;
	}

	public void validateFiles(MultipartFile[] files) {
		Arrays.stream(files).forEach((file) -> validateFile(file));
	}

	@Transactional
	private Job uploadJob(User user, MultipartFile file) throws Exception {
		File uploadedFile = fileStorageService.storeFile(file);
		return saveJob(new Job(UPLOADED, uploadedFile, user));
	}

	public void validateFile(MultipartFile file) {
		if (file.isEmpty()) {
			//			throw new CustomException("7016", HttpStatus.BAD_REQUEST);
		}
		if (!Arrays.stream(mimes).anyMatch(mime -> file.getContentType().equals(mime))) {
			//			throw new CustomException("7014", HttpStatus.BAD_REQUEST, 7015);
		}
	}

	public Job saveJob(Job job) {
		return jobRepository.save(job);
	}

	public void deleteJobsByIDs(List<Integer> IDs, String jwt) {
		deleteJobs(getJobs(IDs), jwt);
	}

	public void deleteJobs(List<Job> jobs, String jwt) {
		List<Job> queued = jobs.stream().filter(job -> !job.getState().equals(UPLOADED)).collect(Collectors.toList());
		pressService.deleteJobsFromPress(queued, jwt);
		jobs.forEach(job -> deleteJob(job));
	}

	public List<Job> getJobs(List<Integer> IDs) {
		return IDs.stream().collect(Collectors.mapping(id -> getOptionalJob(id).get(), Collectors.toList()));
	}

	public Boolean deleteJob(Job job) {
		jobRepository.deleteById(job.getId());
		return true;
	}

	//	public Job getJob(int jobId) {
	//		CustomException ex = new CustomException(E7030, HttpStatus.NOT_FOUND, 7032);
	//		Job job = jobRepository.findById(jobId).orElseThrow(() -> ex);
	//		//		return job;
	//		return null;
	//	}
	public Optional<Job> getOptionalJob(int id) {
		return jobRepository.findById(id);
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
		return jobRepository.findByStateAndUserId(state, userService.getUserByJwt(jwt).getId());
	}

	public void deleteUserJobs(User user) {
		List<Job> jobs = getAllJobs(user);
		jobs.stream().forEach(jobRepository::delete);
	}

	public List<Job> moveJobs(int pressId, List<Integer> IDs, String src, String dst) {
		String srcState = States.get(src), dstState = States.get(dst);
		System.out.println("srcState " + srcState + " dstState " + dstState);
		if (srcState.equals(UPLOADED))
			return pressService.moveTo(pressId, getJobs(IDs), dstState);
		else
			return pressService.moveBetween(pressId, getJobs(IDs), srcState, dstState);
	}
}
