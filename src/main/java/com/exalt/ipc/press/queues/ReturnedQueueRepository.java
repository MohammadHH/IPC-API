package com.exalt.ipc.press.queues;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnedQueueRepository extends CrudRepository<ReturnedQueue, Integer> {
    public Optional<ReturnedQueue> findByPressId(int id);

}
