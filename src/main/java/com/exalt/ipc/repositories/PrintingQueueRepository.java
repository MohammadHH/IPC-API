package com.exalt.ipc.repositories;

import com.exalt.ipc.entities.PrintingQueue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrintingQueueRepository extends CrudRepository<PrintingQueue, Integer> {
	Optional<PrintingQueue> findByPressId(int id);
}
