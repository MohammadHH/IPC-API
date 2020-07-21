package com.exalt.ipc.controllers;

import com.exalt.ipc.entities.File;
import com.exalt.ipc.entities.Job;
import com.exalt.ipc.services.JobService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.exalt.ipc.utilities.Constants.HEADER_STRING;
import static com.exalt.ipc.utilities.States.getStates;


@RestController
@RequestMapping("/v1/users/loggedIn")
@Api(value = "User Management Api", description = "An API to manage job creation,deletion,retrieval and movement")
public class JobController {
	@Autowired
	private JobService jobService;

	@ApiOperation(value = "Upload chosen files", notes = "All the files should be one of these extensions: png,jpeg,pdf",
								response = File.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully uploaded the chosen files"),
			@ApiResponse(code = 400, message = "Failed to upload the chosen files")})
	@PostMapping(value = "/ipc/upload", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Job> uploadJobs(
			@ApiParam(value = "Authorization Bearer utilities", required = true) @RequestHeader(HEADER_STRING) String jwt,
			@RequestParam("file") MultipartFile[] files) {
		if (files == null || files.length == 0) {
			//			new CustomException("7017", HttpStatus.BAD_REQUEST);
		}
		return jobService.uploadJobs(jwt, files);
	}

	@ApiOperation(value = "Get all ipc uploaded files", response = File.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved the uploaded files"),
			@ApiResponse(code = 400, message = "Failed to retrieve the uploaded files")})

	@GetMapping(value = "/ipc/uploaded", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Job> getUploadedJobs(
			@ApiParam(value = "Authorization Bearer utilities", required = true) @RequestHeader(HEADER_STRING) String jwt,
			HttpServletRequest request) {
		return jobService.getUploaded(jwt);
	}

	@GetMapping(value = "/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Job> getAllJobs(
			@ApiParam(value = "Authorization Bearer utilities", required = true) @RequestHeader(HEADER_STRING) String jwt,
			HttpServletRequest request) {
		return jobService.getAllJobs(jwt);
	}

	@DeleteMapping(value = "/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteJobs(
			@ApiParam(value = "Authorization Bearer utilities", required = true) @RequestHeader(HEADER_STRING) String jwt,
			@RequestBody Map<String, Integer[]> IDs) {
		jobService.deleteJobsByIDs(Arrays.asList(IDs.get("IDs")), jwt);
	}

	@GetMapping(value = "/jobs/{state}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Job> getJobsByState(
			@ApiParam(value = "Authorization Bearer utilities", required = true) @RequestHeader(HEADER_STRING) String jwt,
			@PathVariable String state) {
		if (!Arrays.stream(getStates()).anyMatch((s) -> s.equals(state))) {
			//			throw new CustomException("7090", HttpStatus.BAD_REQUEST, 7091);
		}
		return jobService.getJobs(state, jwt);
	}

	@PostMapping(value = "/ipc/jobs/move/{pressId}")
	public List<Job> moveJobs(@RequestHeader(HEADER_STRING) String jwt, @RequestBody Map<String, Integer[]> IDs,
			@PathVariable int pressId, @RequestParam String src, @RequestParam String dst) {
		System.out.println("inside moveJobs() " + Arrays.stream(IDs.get("IDs")));
		return jobService.moveJobs(pressId, Arrays.asList(IDs.get("IDs")), src, dst);
	}

}
