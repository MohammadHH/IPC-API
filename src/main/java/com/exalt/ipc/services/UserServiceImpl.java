package com.exalt.ipc.services;

import com.exalt.ipc.configuration.Encoder;
import com.exalt.ipc.entities.User;
import com.exalt.ipc.exception.ApiException;
import com.exalt.ipc.repositories.UserRepository;
import com.exalt.ipc.requests.SignInRequest;
import com.exalt.ipc.requests.SignUpRequest;
import com.exalt.ipc.requests.UserUpdateRequest;
import com.exalt.ipc.responses.JwtResponse;
import com.exalt.ipc.utilities.Dto;
import com.exalt.ipc.utilities.HelperSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	IPCService ipcService;

	@Autowired
	private JobService jobService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HelperSerivce helperSerivce;

	@Override
	@Transactional
	public User signUP(SignUpRequest signUpRequest, String role) {
		User user = Dto.from(signUpRequest);
		if (!getUserOptional(signUpRequest.getEmail()).isPresent()) {
			User savedUser = userRepository.save(user);
			ipcService.createDBIPC(savedUser);
			return savedUser;
		}
		throw new ApiException(HttpStatus.BAD_REQUEST, 6010, 6011);
	}


	@Override
	public JwtResponse login(SignInRequest signInRequest) {
		//		CustomException customException = new CustomException("7000", HttpStatus.UNAUTHORIZED, 7001);
		User user = getUser(signInRequest.getEmail());
		if (validLogin(signInRequest, user)) {
			return Dto.from(HelperSerivce.buildToken(user));
		} else {
			throw null;
		}
	}

	@Override
	public User getUser(String email) {
		//		CustomException customException = new CustomException("7000", HttpStatus.UNAUTHORIZED, 7001);
		return getUserOptional(email).get();
		//				.orElseThrow(() -> null);
	}

	@Override
	public boolean validLogin(SignInRequest signInRequest, User user) {
		return Encoder.passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
	}

	@Override
	public Optional<User> getUserOptional(String email) {
		return userRepository.findByEmail(email);
	}


	@Override
	public User updateUser(UserUpdateRequest updateRequest, String jwt) {
		User userToUpdate = getUser(helperSerivce.getEmail(jwt));
		userToUpdate.setFirstName(updateRequest.getFirstName());
		userToUpdate.setLastName(updateRequest.getLastName());
		userToUpdate.setPassword(Encoder.passwordEncoder.encode(updateRequest.getPassword()));
		return userRepository.save(userToUpdate);
		//			throw new CustomException("7008", HttpStatus.BAD_REQUEST, 7003, 7004);
	}

	@Override
	public void deleteUser(String jwt) {
		User user = getUser(helperSerivce.getEmail(jwt));
		ipcService.deleteIPC(user);
		userRepository.delete(user);
		jobService.deleteUserJobs(user);
	}

	@Override
	public User getUserByJwt(String jwt) {
		return getUser(helperSerivce.getEmail(jwt));
	}

}
