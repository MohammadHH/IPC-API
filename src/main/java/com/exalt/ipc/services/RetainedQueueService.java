package com.exalt.ipc.services;

import com.exalt.ipc.entities.Press;
import com.exalt.ipc.entities.RetainedQueue;
import com.exalt.ipc.repositories.RetainedQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetainedQueueService {
	@Autowired
	private RetainedQueueRepository retainedQueueRepository;
	
	public RetainedQueue save(Press press) {
		return retainedQueueRepository.save(new RetainedQueue(press));
	}

	public void delete(Press press) {
		retainedQueueRepository.delete(get(press));
	}

	public RetainedQueue get(Press press) {
		return retainedQueueRepository.findByPressId(press.getId()).get();
	}

}
