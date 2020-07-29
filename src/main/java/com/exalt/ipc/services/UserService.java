package com.exalt.ipc.services;

import com.exalt.ipc.configuration.Encoder;
import com.exalt.ipc.entities.User;
import com.exalt.ipc.exception.CommonExceptions;
import com.exalt.ipc.repositories.UserRepository;
import com.exalt.ipc.requests.SignInRequest;
import com.exalt.ipc.requests.SignUpRequest;
import com.exalt.ipc.requests.UserUpdateRequest;
import com.exalt.ipc.responses.JwtResponse;
import com.exalt.ipc.utilities.Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	IPCService ipcService;

	@Autowired
	private JobService jobService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Transactional
	public User signUP(SignUpRequest signUpRequest, String role) {
		User user = Dto.from(signUpRequest);
		if (!getUserOptional(signUpRequest.getEmail()).isPresent()) {
			//No user with the provided email
			User savedUser = userRepository.save(user);
			ipcService.createDBIPC(savedUser);
			return savedUser;
		}
		//There's already a user with the given email
		throw CommonExceptions.USER_REGISTRATION_FAILED_EXCEPTION;
	}


	public JwtResponse login(SignInRequest signInRequest) {
		User user =
				getUserOptional(signInRequest.getEmail()).orElseThrow(() -> CommonExceptions.AUTHENTICATION_FAILED_EXCEPTION);
		if (validPassword(signInRequest, user))
			return Dto.from(JwtService.buildToken(user));
		//wrong password
		throw CommonExceptions.AUTHENTICATION_FAILED_EXCEPTION;
	}

	public User getUser(String email) {
		return getUserOptional(email).get();
	}

	//Does hash(login_password)==DB_hashedPassword
	public boolean validPassword(SignInRequest signInRequest, User user) {
		return Encoder.passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
	}

	public Optional<User> getUserOptional(String email) {
		return userRepository.findByEmail(email);
	}


	public User updateUser(UserUpdateRequest updateRequest, String jwt) {
		User userToUpdate = getUserByJwt(jwt);
		userToUpdate.setFirstName(updateRequest.getFirstName());
		userToUpdate.setLastName(updateRequest.getLastName());
		userToUpdate.setPassword(Encoder.passwordEncoder.encode(updateRequest.getPassword()));
		return userRepository.save(userToUpdate);
	}

	public void deleteUser(String jwt) {
		User user = getUserByJwt(jwt);
		//Delete the user along with his ipc and jobs
		//TODO check this method
		ipcService.deleteIPC(user);
		userRepository.delete(user);
		jobService.deleteUserJobs(user);
	}

	public User getUserByJwt(String jwt) {
		return getUser(jwtService.getEmail(jwt));
	}

}
