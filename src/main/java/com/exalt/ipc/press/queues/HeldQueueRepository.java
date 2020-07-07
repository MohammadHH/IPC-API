package com.exalt.ipc.press.queues;

import com.exalt.ipc.file.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeldQueueRepository extends CrudRepository<HeldQueue, Integer> {
}
