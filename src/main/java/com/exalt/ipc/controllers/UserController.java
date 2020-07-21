package com.exalt.ipc.controllers;

import com.exalt.ipc.entities.User;
import com.exalt.ipc.exception.ApiError;
import com.exalt.ipc.exception.ApiException;
import com.exalt.ipc.exception.ApiValidationError;
import com.exalt.ipc.requests.SignInRequest;
import com.exalt.ipc.requests.SignUpRequest;
import com.exalt.ipc.requests.UserUpdateRequest;
import com.exalt.ipc.responses.JwtResponse;
import com.exalt.ipc.services.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.exalt.ipc.configuration.RoleConstants.ROLE_USER;
import static com.exalt.ipc.utilities.Constants.HEADER_STRING;

@RestController
@RequestMapping(value = "/v1/users/")
@Api(value = "User Management Api", description = "An API to manage user creation,deletion and authentication")
public class UserController {
	@Autowired
	private UserService userService;

	@ApiOperation(value = "Register a normal user")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "User is successfully registered", response = User.class),
			@ApiResponse(code = 400, message = "User registration failed", response = ApiError.class)})
	@PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE,
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public User signUp(
			@ApiParam(value = "Sign up request information from which user entity will be created", required = true) @Valid
			@RequestBody SignUpRequest signUpRequest) {
		return userService.signUP(signUpRequest, ROLE_USER);

	}

	@ApiOperation(value = "Authenticate and login a system user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User successfully authenticated with the given credentials",
									 response = JwtResponse.class),
			@ApiResponse(code = 401, message = "Failed to authenticate user with the given credentials",
									 response = ApiError.class),
			@ApiResponse(code = 400, message = "Non well-formed json", response = ApiValidationError.class)})
	@PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE,
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public JwtResponse signIn(
			@ApiParam(value = "Sign in credentials information for a registered user", required = true) @Valid @RequestBody
					SignInRequest signInRequest) {
		return userService.login(signInRequest);
	}

	@ApiOperation(value = "Get the logged in user information ", notes = "Gets the logged in user his information," +
			"the user to retrieve information for is identified by his utilities", response = User.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retreived user info", response = User.class)})
	@GetMapping(value = "info", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public User getUserInfo(
			@ApiParam(value = "Authorization Bearer jwt token", required = true) @RequestHeader(HEADER_STRING) String jwt) {
		return userService.getUserByJwt(jwt);
	}


	@ApiOperation(value = "Update the logged in user information", notes = "Update a user name and password",
								response = User.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated the user with the new information",
																			response = User.class),
			@ApiResponse(code = 400, message = "Non well-formed json", response = ApiValidationError.class)})
	@PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE,
							produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public User updateUser(
			@ApiParam(value = "Authorization Bearer utilities", required = true) @RequestHeader(HEADER_STRING) String jwt,
			@ApiParam(value = "User update request information from which user entity will be updated", required = true)
			@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
		return userService.updateUser(userUpdateRequest, jwt);
	}

	@ApiOperation(value = "Delete the logged in user ", notes = "This operation deletes a user, " +
			"including his information and registered IPC and all the jobs he added," +
			"  the user to delete is identified by his utilities")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully deleted the user with provided jwt token")})
	@DeleteMapping(value = "delete")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(
			@ApiParam(value = "Authorization Bearer jwt token", required = true) @RequestHeader(HEADER_STRING) String jwt) {
		userService.deleteUser(jwt);
	}


}
