package com.exalt.ipc.controllers;

import com.exalt.ipc.entities.Job;
import com.exalt.ipc.exception.CommonExceptions;
import com.exalt.ipc.services.JobService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.exalt.ipc.utilities.Constants.*;


@RestController
@RequestMapping("/v1/users/loggedIn")
@Api(value = JobController.USER_MANAGEMENT_API, description = JobController.MANAGEMENT_API_DESCRIPTION)
public class JobController {

	public static final String USER_MANAGEMENT_API = "User Management Api";

	public static final String MANAGEMENT_API_DESCRIPTION =
			"An API to manage job creation,deletion,retrieval and movement";

	public static final String UPLOAD_FILES = "Upload chosen files";

	public static final String UPLOAD_FILES_NOTES = "All the files should be one of these extensions: png,jpeg,pdf";

	public static final String UPLOAD_FILES_NOTE = UPLOAD_FILES_NOTES;

	public static final String SUCCESSFULLY_UPLOADED_FILES = "Successfully uploaded the chosen files";

	public static final String UPLOAD_FILES_FAILED = "Failed to upload the chosen files";

	public static final String SUCCESSFULLY_RETRIEVED_UPLOADED_JOBS = "Successfully retrieved the uploaded jobs";

	public static final String GET_ALL_UPLOADED_JOBS = "Get all ipc uploaded jobs";

	public static final String RETRIEVED_ALL_JOBS_SUCCESSFULLY = "Retrieved all jobs successfully";

	public static final String DELETE_JOBS = "Delete jobs with given IDs";

	public static final String SUCCESSFULLY_DELETED_JOBS = "Successfully deleted the jobs";

	public static final String INVALID_JOB_ID = "One or more job has an invalid Id";

	public static final String JOBS_IDS = "IDs of jobs to delete";

	public static final String GET_ALL_JOBS = "Get all jobs";

	public static final String JOB_STATE = "Job state [uploaded,helding,printing,retained]";

	public static final String RETRIEVED_JOBS_SUCESSFULLY = "Retrieved jobs sucessfully";

	public static final String INVALID_JOB_STATE = "Invalid job state";

	public static final String MOVE_JOBS = "Move jobs in press or between presses and queues";

	public static final String JOBS_MOVED_SUCCESSFULLY = "Jobs moved successfully";

	public static final String INVALID_JOB_SOURCE_OR_DESTINATION = "Invalid job source or destination";

	public static final String RETRIEVE_USER_JOBS = "Retrieve user's job in a given state";

	@Autowired
	private JobService jobService;

	@ApiOperation(value = UPLOAD_FILES, notes = UPLOAD_FILES_NOTE, response = Job.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_UPLOADED_FILES),
			@ApiResponse(code = BAD_REQUEST, message = UPLOAD_FILES_FAILED),
			@ApiResponse(code = UNSUPPORTED_MEDIA_TYPE, message = UPLOAD_FILES_FAILED)})
	@PostMapping(value = "/ipc/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public List<Job> uploadJobs(@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(required = true) @RequestParam("file") MultipartFile[] files) {
		
		return jobService.uploadJobs(jwt, files);
	}

	@ApiOperation(value = GET_ALL_UPLOADED_JOBS, response = Job.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_RETRIEVED_UPLOADED_JOBS)})
	@GetMapping(value = "/ipc/uploaded", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public List<Job> getUploadedJobs(
			@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt) {
		return jobService.getUploaded(jwt);
	}

	@ApiOperation(value = GET_ALL_JOBS, response = Job.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = RETRIEVED_ALL_JOBS_SUCCESSFULLY)})
	@GetMapping(value = "/jobs", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public List<Job> getAllJobs(@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt) {
		return jobService.getAllJobs(jwt);
	}

	@ApiOperation(value = DELETE_JOBS)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_DELETED_JOBS),
			@ApiResponse(code = NOT_FOUND, message = INVALID_JOB_ID)})
	@DeleteMapping(value = "/jobs", consumes = {MediaType.APPLICATION_JSON_VALUE},
								 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public void deleteJobs(@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(value = JOBS_IDS, required = true) @RequestBody Map<String, Integer[]> IDs) {
		assertIDsNotNull(IDs);
		jobService.deleteJobsByIDs(Arrays.asList(IDs.get("IDs")), jwt);
	}

	@ApiOperation(value = RETRIEVE_USER_JOBS, response = Job.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = RETRIEVED_JOBS_SUCESSFULLY),
			@ApiResponse(code = BAD_REQUEST, message = INVALID_JOB_STATE)})
	@GetMapping(value = "/jobs/{state}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public List<Job> getJobsByState(
			@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(name = JOB_STATE, required = true) @PathVariable String state) {
		return jobService.getJobs(state, jwt);
	}

	@ApiOperation(value = MOVE_JOBS, response = Job.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = JOBS_MOVED_SUCCESSFULLY),
			@ApiResponse(code = BAD_REQUEST, message = INVALID_JOB_SOURCE_OR_DESTINATION)})
	@PostMapping(value = "/ipc/jobs/move/{pressId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public List<Job> moveJobs(@RequestHeader(AUTHORIZATION) String jwt, @RequestBody Map<String, Integer[]> IDs,
			@PathVariable int pressId, @RequestParam String src, @RequestParam String dst) {
		assertIDsNotNull(IDs);
		return jobService.moveJobs(pressId, Arrays.asList(IDs.get("IDs")), src, dst, jwt);
	}

	public void assertIDsNotNull(Map<String, Integer[]> IDs) {
		if (IDs.get("IDs") == null)
			throw CommonExceptions.NULL_JOB_IDS_EXCEPTION;
	}

}
