package com.exalt.ipc.press.queues;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnedQueueRepository extends CrudRepository<ReturnedQueue, Integer> {
}
