package com.exalt.ipc.press.queues;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeldQueueRepository extends CrudRepository<HeldQueue, Integer> {
    public Optional<HeldQueue> findByPressId(int id);
}
