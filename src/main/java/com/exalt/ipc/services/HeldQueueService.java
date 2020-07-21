package com.exalt.ipc.services;

import com.exalt.ipc.entities.HeldQueue;
import com.exalt.ipc.entities.Press;
import com.exalt.ipc.repositories.HeldQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeldQueueService {

	@Autowired
	private HeldQueueRepository heldQueueRepository;

	public HeldQueueService() {
	}

	public HeldQueue save(Press press, long sizeLimit) {
		return heldQueueRepository.save(new HeldQueue(press, sizeLimit));
	}

	public void delete(Press press) {
		heldQueueRepository.delete(get(press));
	}

	public HeldQueue get(Press press) {
		return heldQueueRepository.findByPressId(press.getId()).get();
	}
}
