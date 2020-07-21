package com.exalt.ipc.repositories;

import com.exalt.ipc.entities.RetainedQueue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetainedQueueRepository extends CrudRepository<RetainedQueue, Integer> {
	public Optional<RetainedQueue> findByPressId(int id);
}
