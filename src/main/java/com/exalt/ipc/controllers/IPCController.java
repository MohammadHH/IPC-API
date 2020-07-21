package com.exalt.ipc.controllers;

import com.exalt.ipc.entities.IPC;
import com.exalt.ipc.requests.IPCUpdateRequest;
import com.exalt.ipc.responses.IPCResponse;
import com.exalt.ipc.services.IPCService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "IPC Management Api", description = "An API to manage ipc update and retrieval")
public class IPCController {

	@Autowired
	private IPCService ipcService;

	@ApiOperation(value = "Get a user ipc",
								notes = "Gets a logged user its ipc information and the residual files available for upload",
								response = IPC.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved the ipc for the provided user"),
			@ApiResponse(code = 404, message = "Couldn't get the ipc associated with this user"),})
	@GetMapping(value = "/v1/users/loggedIn/ipc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public IPCResponse getUserIPC(
			@ApiParam(value = "Authorization Bearer jwt token", required = true) @RequestHeader("Authorization") String jwt) {
		return ipcService.ipcByUserJwt(jwt);
	}

	@PutMapping(value = "/v1/users/loggedIn/ipc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public IPCResponse updateUserIPC(
			@ApiParam(value = "Authorization Bearer jwt token", required = true) @RequestHeader("Authorization") String jwt,
			@Valid @RequestBody IPCUpdateRequest ipcUpdateRequest) {
		return ipcService.updateIPC(jwt, ipcUpdateRequest);
	}


}
