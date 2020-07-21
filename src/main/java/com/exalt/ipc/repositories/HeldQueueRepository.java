package com.exalt.ipc.repositories;

import com.exalt.ipc.entities.HeldQueue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeldQueueRepository extends CrudRepository<HeldQueue, Integer> {
	Optional<HeldQueue> findByPressId(int id);
}
