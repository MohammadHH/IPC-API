package com.exalt.ipc.services;

import com.exalt.ipc.entities.IPC;
import com.exalt.ipc.entities.User;
import com.exalt.ipc.repositories.IPCRepository;
import com.exalt.ipc.requests.IPCUpdateRequest;
import com.exalt.ipc.responses.IPCResponse;
import com.exalt.ipc.utilities.Dto;
import com.exalt.ipc.utilities.HelperSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IPCService {
	public static final String LOCAL_HOST = "localhost";

	@Autowired
	IPCRepository ipcRepository;

	@Autowired
	UserService userService;

	@Autowired
	HelperSerivce helperSerivce;

	@Autowired
	private JobService jobService;

	@Transactional
	public IPCResponse ipcByUserJwt(String jwt) {
		User user = userService.getUserByJwt(jwt);
		return Dto.from(getIPC(user.getId()), getResidual(jwt));
	}

	@Transactional
	public IPC getIPC(int userId) {
		return ipcRepository.findByUserId(userId).get();
	}

	public Optional<IPC> getIPCByPressId(int pressId) {
		return ipcRepository.findByPressId(pressId);
	}

	public IPC saveIPC(IPC ipc) {
		return ipcRepository.save(ipc);
	}

	public int getResidual(String jwt) {
		return getIPC(userService.getUserByJwt(jwt).getId()).getQueueLimit() - jobService.getUploaded(jwt).size();
	}

	@Transactional
	public IPC createDBIPC(User user) {
		IPC ipc = new IPC();
		ipc.setAddress(LOCAL_HOST);
		ipc.setUser(user);
		return ipcRepository.save(ipc);
	}

	public void deleteIPC(User user) {
		ipcRepository.delete(ipcRepository.findByUserId(user.getId()).get());
	}

	public boolean isMapped(IPC ipc) {
		return ipc.getPress() != null;
	}

	@Transactional
	public IPCResponse updateIPC(String jwt, IPCUpdateRequest ipcUpdateRequest) {
		User user = userService.getUserByJwt(jwt);
		IPC ipc = getIPC(user.getId());
		ipc.setAddress(ipcUpdateRequest.getAddress());
		return Dto.from(ipc, getResidual(jwt));
	}
}
