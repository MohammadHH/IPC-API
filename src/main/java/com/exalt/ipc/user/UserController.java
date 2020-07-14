package com.exalt.ipc.user;

import com.exalt.ipc.ipc.IPCService;
import com.exalt.ipc.localization.LocaleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.exalt.ipc.configuration.Constants.HEADER_STRING;
import static com.exalt.ipc.configuration.Constants.TOKEN_PREFIX;
import static com.exalt.ipc.configuration.RoleConstants.ROLE_USER;

@RestController
@Api(value = "User Management API")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LocaleService localeService;
    @Autowired
    private IPCService ipcService;

    @ApiOperation(value = "Register a user with role ROLE_USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User is successfully registered with the given information", response = User.class),
            @ApiResponse(code = 400, message = "Non well-formed json")
    })
    @PostMapping(value = {"/v1/users/signup"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signUp(
            @ApiParam(value = "Sign up request information from which user entity will be created",
                    required = true)
            @Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
        User user = userService.signUP(signUpRequest, ROLE_USER, request);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Authenticate A System User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully authenticated with the given credentials"),
            @ApiResponse(code = 401, message = "Failed to authenticate user with the given credentials"),
            @ApiResponse(code = 400, message = "Non well-formed json")
    })
    @PostMapping(value = {"/v1/users/login"})
    public ResponseEntity<Void> signIn(
            @ApiParam(value = "Sign in credentials information for a registered user",
                    required = true) @Valid @RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        String token = userService.login(signInRequest, request);
        if (token != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HEADER_STRING, TOKEN_PREFIX + token);
            return ResponseEntity.ok().headers(httpHeaders).build();
        } else return null;
    }

    @ApiOperation(value = "Get user information ", notes = "Gets the logged in user his information," +
            "the user to retrieve information for is identified by his helpers", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retreived user info"),
            @ApiResponse(code = 404, message = "Couldn't retrieve a user with the provided helpers")})
    @GetMapping(value = "/v1/users/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserInfo(@ApiParam(value = "Authorization Bearer helpers",
            required = true) @RequestHeader(HEADER_STRING) String jwt, HttpServletRequest request) {
        return ResponseEntity.ok(userService.getUser(jwt, request));
    }

    @ApiOperation(value = "Delete a user ", notes = "This operation completely deletes a user, " +
            "including his information and registered IPC and all the jobs and presses he added," +
            "  the user to delete is identified by his helpers",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the user with the provided helpers"),
            @ApiResponse(code = 404, message = "Couldn't delete a user with the provided helpers")})
    @DeleteMapping("/v1/users/delete")
    public ResponseEntity<Void> deleteUser(@ApiParam(value = "Authorization Bearer helpers",
            required = true) @RequestHeader(HEADER_STRING) String jwt, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update a user information", notes = "Update the user with the given information, the user to update is identified by his helpers",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the user with the new information"),
            @ApiResponse(code = 400, message = "Non well-formed json"),
            @ApiResponse(code = 404, message = "Couldn't update the user with the provided helpers"),
    })
    @PutMapping(value = "/v1/users/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@ApiParam(value = "Authorization Bearer helpers",
            required = true) @RequestHeader(HEADER_STRING) String jwt,
                                           @ApiParam(value = "Sign up request information from which user entity will be created", required = true)
                                           @Valid @RequestBody SignUpRequest updateRequest, HttpServletRequest request) {
        return ResponseEntity.ok(userService.updateUser(updateRequest, request));
    }

}
