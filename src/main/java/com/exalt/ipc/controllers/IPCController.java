package com.exalt.ipc.controllers;

import com.exalt.ipc.requests.IPCUpdateRequest;
import com.exalt.ipc.responses.IPCResponse;
import com.exalt.ipc.services.IPCService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.exalt.ipc.utilities.Constants.AUTHORIZATION;
import static com.exalt.ipc.utilities.Constants.JWT_TOKEN;

@RestController
@Api(value = IPCController.IPC_MANAGEMENT_API, description = IPCController.IPC_API_DESCRIPTION)
public class IPCController {

	public static final String IPC_MANAGEMENT_API = "IPC Management Api";

	public static final String IPC_API_DESCRIPTION = "An API to manage ipc update and retrieval";

	public static final String GET_USER_IPC = "Get a user ipc";

	public static final String GETS_LOGGED_USER_IPC_INFORMATION =
			"Gets a logged user its ipc information and the residual files available for upload";

	public static final int OK = 200;

	public static final String SUCCESSFULLY_RETRIEVED_IPC = "Successfully retrieved the ipc for the provided user";

	public static final String IPC_ADDRESS = "IPC new ip address";

	public static final String UPDATE_USER_IPC = "Update a user ipc";

	public static final String UPDATE_IPC_WITH_ADDRESS = "Update the user ipc with the given address";

	public static final String SUCCESSFULLY_UPDATED_USER_IPC = "Successfully updated user ipc";


	@Autowired
	private IPCService ipcService;

	@ApiOperation(value = GET_USER_IPC, notes = GETS_LOGGED_USER_IPC_INFORMATION, response = IPCResponse.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_RETRIEVED_IPC)})
	@GetMapping(value = "/v1/users/loggedIn/ipc",
							produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public IPCResponse getUserIPC(
			@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt) {
		return ipcService.ipcByUserJwt(jwt);
	}

	@ApiOperation(value = UPDATE_USER_IPC, notes = UPDATE_IPC_WITH_ADDRESS, response = IPCResponse.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_UPDATED_USER_IPC)})
	@PutMapping(value = "/v1/users/loggedIn/ipc", consumes = MediaType.APPLICATION_JSON_VALUE,
							produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public IPCResponse updateUserIPC(
			@ApiParam(value = JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(value = IPC_ADDRESS, required = true) @Valid @RequestBody IPCUpdateRequest ipcUpdateRequest) {
		return ipcService.updateIPC(jwt, ipcUpdateRequest);
	}


}
