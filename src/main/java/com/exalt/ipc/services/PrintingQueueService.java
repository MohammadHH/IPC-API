package com.exalt.ipc.services;

import com.exalt.ipc.entities.Press;
import com.exalt.ipc.entities.PrintingQueue;
import com.exalt.ipc.repositories.PrintingQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrintingQueueService {
	@Autowired
	private PrintingQueueRepository printingQueueRepository;

	public PrintingQueueService() {
	}

	public PrintingQueue save(Press press, int limit) {
		return printingQueueRepository.save(new PrintingQueue(press, limit));
	}

	public void delete(Press press) {
		printingQueueRepository.delete(get(press));
	}

	public PrintingQueue get(Press press) {
		return printingQueueRepository.findByPressId(press.getId()).get();
	}
}
