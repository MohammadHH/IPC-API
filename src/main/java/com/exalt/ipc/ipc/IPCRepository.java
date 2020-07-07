package com.exalt.ipc.ipc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPCRepository extends CrudRepository<IPC, Integer> {
}
