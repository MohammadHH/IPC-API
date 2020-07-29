package com.exalt.ipc.controllers;

import com.exalt.ipc.entities.Job;
import com.exalt.ipc.entities.Press;
import com.exalt.ipc.exception.ApiError;
import com.exalt.ipc.requests.PressRequest;
import com.exalt.ipc.responses.MappedPressInfoResponse;
import com.exalt.ipc.responses.PressResponse;
import com.exalt.ipc.services.PressService;
import com.exalt.ipc.utilities.Constants;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.exalt.ipc.utilities.Constants.AUTHORIZATION;
import static com.exalt.ipc.utilities.Constants.JWT_TOKEN;

@RestController
@Api(value = PressController.PRESS_MANAGEMENT_API, description = PressController.PRESS_API_DESCRIPTION)
public class PressController {
	public static final String PRESS_MANAGEMENT_API = "Press Management Api";

	public static final String PRESS_API_DESCRIPTION =
			"An API to manage press creation,update,deletion,retrieval,mapping and unmapping in addition to managing press printing requests";

	public static final String ADD_A_PRESS_CONTAINER = "Add a press container";

	public static final String PRESS__SUCCESSFULLY_CREATED = "Press controller is successfully created";

	public static final String PRESS_SUCCESSFULLY_CREATED = PRESS__SUCCESSFULLY_CREATED;

	public static final String PRESS_REQUEST = "Press address,name and description";

	public static final String RETRIEVED_PRESS_SUCCESSFULLY = "Retrieved press successfully";

	public static final String NO_PRESS_WITH_GIVEN_ID = "No press with the given Id";

	public static final String RETRIEVE_PRESS_CONTAINER = "Retrieve press container info";

	public static final String UPDATE_PRESS = "Update a press information";

	public static final String PRESS_SUCCESSFULLY_UPDATED = "The press is successfully updated";

	public static final String DELETE_PRESS = "Delete a press";

	public static final String SUCCESSFULLY_DELETED_PRESS = "Successfully deleted the press";

	public static final String MAP_PRESS = "Map a press";

	public static final String SUCCESSFULLY_MAPPED_PRESS = "Successfully mapped the press";

	public static final String UNMAP_PRESS = "Unmap a press";

	public static final String PRESS_IS_ALREADY_MAPPED = "Press is already mapped";

	public static final String SUCCESSFULLY_UNMAPPED_PRESS = "Successfully unmapped the press";

	public static final String PRESS_IS_NOW_PRINTING_Front_JOB = "The job at front of your press is now printing";

	private static final String PRESS_IS_ALREADY_UNMAPPED = "Press is already unmapped";

	public static final String PRINT_PRESS_FRONT_JOB = "Print press front job";

	public static final String PRINTING_PRESS_JOB_HAS_FAILED = "Printing press job has failed";

	public static final String PRESS_IS_NOW_PRINTING_ALL_OF_YOUR_JOBS =
			"Press is now printing all of your jobs at your mapped press";

	public static final String RETRIEVE_MAPPED_PRESS_INFO = "Retrieve info of a mapped press";

	public static final String SUCCESSFULLY_RETRIEVED_MAPPED_PRESS_INFO =
			"Successfully retrieved info of your ipc's mapped press";

	public static final String FAILED_RETRIEVE_MAPPED_PRESS_INFO = "Failed to retrieve mapped press info";

	@Autowired
	PressService pressService;

	@ApiOperation(value = ADD_A_PRESS_CONTAINER)
	@ApiResponses(
			value = {@ApiResponse(code = Constants.CREATED, message = PRESS_SUCCESSFULLY_CREATED, response = Press.class)})
	@PostMapping(value = "/v1/users/loggedIn/presses/", consumes = MediaType.APPLICATION_JSON_VALUE,
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public Press addPress(
			@ApiParam(value = PRESS_REQUEST, required = true) @Valid @RequestBody PressRequest pressRequest) {
		return pressService.addPress(pressRequest);
	}

	@ApiOperation(value = RETRIEVE_PRESS_CONTAINER)
	@ApiResponses(value = {
			@ApiResponse(code = Constants.OK, message = RETRIEVED_PRESS_SUCCESSFULLY, response = PressResponse.class),
			@ApiResponse(code = Constants.NOT_FOUND, message = NO_PRESS_WITH_GIVEN_ID, response = ApiError.class)})
	@GetMapping(value = "/v1/users/loggedIn/presses/{pressId}",
							produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public PressResponse getPress(@ApiParam(required = true) @PathVariable int pressId) {
		return pressService.getPressInfo(pressId);
	}

	@ApiOperation(value = UPDATE_PRESS)
	@ApiResponses(
			value = {@ApiResponse(code = Constants.OK, message = PRESS_SUCCESSFULLY_UPDATED, response = PressResponse.class),
					@ApiResponse(code = Constants.NOT_FOUND, message = NO_PRESS_WITH_GIVEN_ID, response = ApiError.class)})
	@PutMapping(value = "/v1/users/loggedIn/presses/{pressId}", consumes = MediaType.APPLICATION_JSON_VALUE,
							produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public PressResponse updatePress(
			@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(value = PRESS_REQUEST, required = true) @Valid @RequestBody PressRequest pressRequest,
			@ApiParam(required = true) @PathVariable int pressId) {
		return pressService.updatePress(pressId, pressRequest);
	}

	@ApiOperation(value = DELETE_PRESS)
	@ApiResponses(value = {@ApiResponse(code = Constants.OK, message = SUCCESSFULLY_DELETED_PRESS),
			@ApiResponse(code = Constants.NOT_FOUND, message = NO_PRESS_WITH_GIVEN_ID, response = ApiError.class)})
	@DeleteMapping(value = "/v1/users/loggedIn/presses/{pressId}",
								 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public void deletePress(@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(required = true) @PathVariable int pressId) {
		pressService.deletePress(pressId, jwt);
	}


	@ApiOperation(value = MAP_PRESS)
	@ApiResponses(
			value = {@ApiResponse(code = Constants.OK, message = SUCCESSFULLY_MAPPED_PRESS, response = PressResponse.class),
					@ApiResponse(code = Constants.NOT_FOUND, message = NO_PRESS_WITH_GIVEN_ID, response = ApiError.class),
					@ApiResponse(code = Constants.BAD_REQUEST, message = PRESS_IS_ALREADY_MAPPED, response = ApiError.class)})
	@PostMapping(value = "/v1/users/loggedIn/presses/{pressId}/map",
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public PressResponse mapPress(@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(required = true) @PathVariable int pressId) {
		return pressService.map(pressId, jwt);
	}

	@ApiOperation(value = UNMAP_PRESS)
	@ApiResponses(
			value = {@ApiResponse(code = Constants.OK, message = SUCCESSFULLY_UNMAPPED_PRESS, response = PressResponse.class),
					@ApiResponse(code = Constants.NOT_FOUND, message = NO_PRESS_WITH_GIVEN_ID, response = ApiError.class),
					@ApiResponse(code = Constants.BAD_REQUEST, message = PRESS_IS_ALREADY_UNMAPPED, response = ApiError.class)})
	@PostMapping(value = "/v1/users/loggedIn/presses/{pressId}/unmap",
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public PressResponse unmapPress(
			@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(required = true) @PathVariable int pressId) {
		return pressService.unmap(pressId);
	}

	@ApiOperation(value = PRINT_PRESS_FRONT_JOB)
	@ApiResponses(
			value = {@ApiResponse(code = Constants.OK, message = PRESS_IS_NOW_PRINTING_Front_JOB, response = Job.class),
					@ApiResponse(code = Constants.BAD_REQUEST, message = PRINTING_PRESS_JOB_HAS_FAILED,
											 response = ApiError.class)})
	@PostMapping(value = "/v1/users/loggedIn/presses/mapped/printFront",
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public Job printJob(@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt) {
		return pressService.printFront(jwt);
	}


	@ApiOperation(value = PRINT_PRESS_FRONT_JOB)
	@ApiResponses(value = {
			@ApiResponse(code = Constants.OK, message = PRESS_IS_NOW_PRINTING_ALL_OF_YOUR_JOBS, response = Job.class),
			@ApiResponse(code = Constants.BAD_REQUEST, message = PRINTING_PRESS_JOB_HAS_FAILED, response = ApiError.class)})
	@PostMapping(value = "/v1/users/loggedIn/presses/mapped/printAll",
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public List<Job> printAllJob(@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt) {
		return pressService.printAll(jwt);
	}

	@ApiOperation(value = RETRIEVE_MAPPED_PRESS_INFO)
	@ApiResponses(value = {@ApiResponse(code = Constants.OK, message = SUCCESSFULLY_RETRIEVED_MAPPED_PRESS_INFO,
																			response = MappedPressInfoResponse.class),
			@ApiResponse(code = Constants.BAD_REQUEST, message = FAILED_RETRIEVE_MAPPED_PRESS_INFO,
									 response = ApiError.class)})
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/v1/users/loggedIn/presses/mapped/info",
							produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public MappedPressInfoResponse printPressInfo(
			@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt) {
		return pressService.getMappedPressInfo(jwt);
	}


}
