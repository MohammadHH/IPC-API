package com.exalt.ipc.utilities;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.exalt.ipc.configuration.Encoder;
import com.exalt.ipc.entities.File;
import com.exalt.ipc.entities.IPC;
import com.exalt.ipc.entities.Press;
import com.exalt.ipc.entities.User;
import com.exalt.ipc.requests.PressRequest;
import com.exalt.ipc.requests.SignUpRequest;
import com.exalt.ipc.responses.IPCResponse;
import com.exalt.ipc.responses.JwtResponse;
import com.exalt.ipc.responses.PressResponse;
import org.springframework.web.multipart.MultipartFile;

import static com.exalt.ipc.configuration.RoleConstants.ROLE_USER;

public class Dto {

	public static User from(SignUpRequest signUpRequest) {
		User user = new User();
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(Encoder.passwordEncoder.encode(signUpRequest.getPassword()));
		user.setRole(ROLE_USER);
		return user;
	}

	public static PressResponse from(Press press, boolean mapped) {
		PressResponse pressResponse = new PressResponse();
		pressResponse.setId(press.getId());
		pressResponse.setName(press.getName());
		pressResponse.setAddress(press.getAddress());
		pressResponse.setDescription(press.getDescription());
		pressResponse.setCreationDate(press.getCreationDate());
		pressResponse.setMapped(mapped);
		return pressResponse;
	}

	public static JwtResponse from(String jwt) {
		DecodedJWT decodedJWT = HelperSerivce.getDecodedJwt(jwt);
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setEmail(decodedJWT.getSubject());
		jwtResponse.setExpiration(decodedJWT.getExpiresAt());
		jwtResponse.setRole(decodedJWT.getClaim("role").asString());
		jwtResponse.setToken(jwt);
		return jwtResponse;
	}

	public static IPCResponse from(IPC ipc, int residual) {
		IPCResponse ipcResponse = new IPCResponse();
		ipcResponse.setAddress(ipc.getAddress());
		ipcResponse.setCreationDate(ipc.getCreationDate());
		ipcResponse.setId(ipc.getId());
		ipcResponse.setQueueLimit(ipc.getQueueLimit());
		ipcResponse.setResidual(residual);
		return ipcResponse;
	}

	public static File from(MultipartFile multipartFile) {
		File file = new File();
		file.setName(multipartFile.getOriginalFilename());
		file.setSize(multipartFile.getSize());
		file.setType(multipartFile.getContentType());
		return file;
	}

	public static Press from(PressRequest pressRequest) {
		return new Press(pressRequest.getName(), pressRequest.getAddress(), pressRequest.getDescription());
	}
}
