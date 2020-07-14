package com.exalt.ipc.press.queues;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrintingQueueRepository extends CrudRepository<PrintingQueue, Integer> {
    public Optional<PrintingQueue> findByPressId(int id);
}
