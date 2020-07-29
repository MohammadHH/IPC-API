package com.exalt.ipc.controllers;

import com.exalt.ipc.entities.User;
import com.exalt.ipc.exception.ApiError;
import com.exalt.ipc.requests.SignInRequest;
import com.exalt.ipc.requests.SignUpRequest;
import com.exalt.ipc.requests.UserUpdateRequest;
import com.exalt.ipc.responses.JwtResponse;
import com.exalt.ipc.services.UserService;
import com.exalt.ipc.utilities.Constants;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.exalt.ipc.configuration.RoleConstants.ROLE_USER;
import static com.exalt.ipc.utilities.Constants.AUTHORIZATION;

@RestController
@RequestMapping(value = "/v1/users/")
@Api(value = UserController.USER_MANAGEMENT_API, description = UserController.USER_API_DESCRIPTION)
public class UserController {

	public static final String REGISTER_A_NORMAL_USER = "Register a normal user";

	public static final String USER_IS_SUCCESSFULLY_REGISTERED = "User is successfully registered";

	public static final String USER_REGISTRATION_FAILED = "User registration failed";

	public static final String INFORMATION_FROM_WHICH_USER_ENTITY_WILL_BE_CREATED =
			"Sign up request information from which user entity will be created";

	public static final String LOGIN_A_SYSTEM_USER = "Authenticate and login a system user";

	public static final String SUCCESSFULLY_AUTHENTICATED = "User successfully authenticated with the given credentials";

	public static final String FAILED_TO_AUTHENTICATE = "Failed to authenticate user with the given credentials";

	public static final String INVALID_REQUEST_PARAMETERS = "Invalid request parameters";

	public static final String SIGN_IN_CREDENTIALS = "Sign in credentials information for a registered user";

	public static final String LOGGED_IN_USER_INFORMATION = "Get the logged in user information ";

	public static final String RETRIEVE_USER_INFORMATION =
			"Gets the logged in user his information,the user to retrieve information for is identified by his token claims";

	public static final String SUCCESSFULLY_RETRIEVED_USER_INFO = "Successfully retrieved user info";

	public static final String UPDATE_LOGGED_IN_USER = "Update the logged in user information";

	public static final String UPDATE_NAME_AND_PASSWORD = "Update a user name and password";

	public static final String SUCCESSFULLY_UPDATED = "Successfully updated the user with the new information";

	public static final String USER_UPDATE_REQUEST =
			"User update request information from which user entity will be updated";

	public static final String DELETE_USER = "Delete the logged in user ";

	public static final String DELETE_All_USER_INFORMATION =
			"This operation deletes a user,including his information and registered IPC and all the jobs he added,the user to delete is identified by his jwt claims";

	public static final String SUCCESSFULLY_DELETED = "Successfully deleted the user with provided jwt token";

	public static final String USER_MANAGEMENT_API = "User Management Api";

	public static final String USER_API_DESCRIPTION = "An API to manage user creation,deletion and authentication";

	public static final int OK = 200;

	public static final int BAD_REQUEST = 400;

	public static final int UNAUTHORIZED = 401;


	@Autowired
	private UserService userService;

	@ApiOperation(value = REGISTER_A_NORMAL_USER)
	@ApiResponses(value = {@ApiResponse(code = OK, message = USER_IS_SUCCESSFULLY_REGISTERED, response = User.class),
			@ApiResponse(code = BAD_REQUEST, message = USER_REGISTRATION_FAILED, response = ApiError.class)})
	@PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE,
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public User signUp(
			@ApiParam(value = INFORMATION_FROM_WHICH_USER_ENTITY_WILL_BE_CREATED, required = true) @Valid @RequestBody
					SignUpRequest signUpRequest) {
		return userService.signUP(signUpRequest, ROLE_USER);

	}

	@ApiOperation(value = LOGIN_A_SYSTEM_USER)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_AUTHENTICATED, response = JwtResponse.class),
			@ApiResponse(code = UNAUTHORIZED, message = FAILED_TO_AUTHENTICATE, response = ApiError.class)})
	@PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE,
							 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public JwtResponse signIn(
			@ApiParam(value = SIGN_IN_CREDENTIALS, required = true) @Valid @RequestBody SignInRequest signInRequest) {
		return userService.login(signInRequest);
	}

	@ApiOperation(value = LOGGED_IN_USER_INFORMATION, notes = RETRIEVE_USER_INFORMATION, response = User.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_RETRIEVED_USER_INFO, response = User.class)})
	@GetMapping(value = "loggedIn", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public User getUserInfo(
			@ApiParam(value = Constants.JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt) {
		return userService.getUserByJwt(jwt);
	}


	@ApiOperation(value = UPDATE_LOGGED_IN_USER, notes = UPDATE_NAME_AND_PASSWORD, response = User.class)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_UPDATED, response = User.class)})
	@PutMapping(value = "loggedIn", consumes = MediaType.APPLICATION_JSON_VALUE,
							produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public User updateUser(
			@ApiParam(value = Constants.JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt,
			@ApiParam(value = USER_UPDATE_REQUEST, required = true) @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
		return userService.updateUser(userUpdateRequest, jwt);
	}

	@ApiOperation(value = DELETE_USER, notes = DELETE_All_USER_INFORMATION)
	@ApiResponses(value = {@ApiResponse(code = OK, message = SUCCESSFULLY_DELETED)})
	@DeleteMapping(value = "loggedIn")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(
			@ApiParam(value = Constants.JWT_TOKEN, required = true) @RequestHeader(AUTHORIZATION) String jwt) {
		userService.deleteUser(jwt);
	}


}
