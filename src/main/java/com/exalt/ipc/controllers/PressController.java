package com.exalt.ipc.controllers;

import com.exalt.ipc.entities.Job;
import com.exalt.ipc.entities.Press;
import com.exalt.ipc.requests.PressRequest;
import com.exalt.ipc.responses.MappedPressInfoResponse;
import com.exalt.ipc.responses.PressResponse;
import com.exalt.ipc.services.PressService;
import com.exalt.ipc.services.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.exalt.ipc.utilities.Constants.HEADER_STRING;

@RestController
@Api(value = "Press Management Api", description = "An API to manage press creation,update,deletion,retrieval," +
		"mapping and unmapping in addition to managing press printing requests")
public class PressController {
	@Autowired
	PressService pressService;

	@Autowired
	private UserService userService;


	@ApiOperation(value = "Add a press container")
	@ApiResponses(
			value = {@ApiResponse(code = 201, message = "Press controller is successfully created", response = Press.class),
					@ApiResponse(code = 400, message = "Non well-formed json")})
	@PostMapping(value = "/v1/users/loggedIn/presses/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Press addPress(@Valid @RequestBody PressRequest pressRequest) {
		return pressService.storePress(pressRequest);
	}

	@GetMapping("/v1/users/loggedIn/presses/{pressId}")
	public PressResponse getPress(@PathVariable int pressId) {
		return pressService.getPressInfo(pressId);
	}

	@PutMapping(value = "/v1/users/loggedIn/presses/{pressId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public PressResponse updatePress(
			@ApiParam(value = "Jwt Bearer", required = true) @RequestHeader(HEADER_STRING) String jwt,
			@Valid @RequestBody PressRequest pressRequest, @PathVariable int pressId) {
		return pressService.updatePress(pressId, pressRequest);
	}

	@DeleteMapping(value = "/v1/users/loggedIn/presses/{pressId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void deletePress(@ApiParam(value = "Jwt Bearer", required = true) @RequestHeader(HEADER_STRING) String jwt,
			@PathVariable int pressId) {
		pressService.deletePress(pressId, jwt);
	}

	@PostMapping("/v1/users/loggedIn/presses/{pressId}/map")
	@ResponseStatus(HttpStatus.OK)
	public Press mapPress(@ApiParam(value = "Jwt Bearer", required = true) @RequestHeader(HEADER_STRING) String jwt,
			@ApiParam(value = "pressId", required = true) @PathVariable int pressId) {
		return pressService.map(pressId, jwt);
	}

	@PostMapping("/v1/users/loggedIn/presses/{pressId}/unmap")
	public PressResponse unmapPress(@RequestHeader(HEADER_STRING) String jwt, @PathVariable int pressId) {
		return pressService.unmap(pressId);
	}


	@PostMapping("/v1/users/loggedIn/presses/mapped/printFront")
	public Job printJob(@RequestHeader(HEADER_STRING) String jwt) {
		return pressService.printFront(jwt);
	}

	@PostMapping("/v1/users/loggedIn/presses/mapped/printAll")
	public List<Job> printAllJob(@RequestHeader(HEADER_STRING) String jwt) {
		return pressService.printAll(jwt);
	}

	@GetMapping("/v1/users/loggedIn/presses/mapped/info")
	public MappedPressInfoResponse printPressInfo(@RequestHeader(HEADER_STRING) String jwt) {
		return pressService.getMappedPressInfo(jwt);
	}


}
