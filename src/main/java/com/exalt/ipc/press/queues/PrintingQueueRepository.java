package com.exalt.ipc.press.queues;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrintingQueueRepository extends CrudRepository<PrintingQueue, Integer> {
}
