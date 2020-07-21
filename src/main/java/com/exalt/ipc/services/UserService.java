package com.exalt.ipc.services;

import com.exalt.ipc.entities.User;
import com.exalt.ipc.requests.SignInRequest;
import com.exalt.ipc.requests.SignUpRequest;
import com.exalt.ipc.requests.UserUpdateRequest;
import com.exalt.ipc.responses.JwtResponse;

import java.util.Optional;

public interface UserService {

	//	@Transactional
	User signUP(SignUpRequest signUpRequest, String role);

	JwtResponse login(SignInRequest signInRequest);

	User getUser(String email);

	boolean validLogin(SignInRequest signInRequest, User user);

	Optional<User> getUserOptional(String email);


	User updateUser(UserUpdateRequest updateRequest, String jwt);

	void deleteUser(String jwt);

	User getUserByJwt(String jwt);
}
