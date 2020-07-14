package com.exalt.ipc.ipc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPCRepository extends CrudRepository<IPC, Integer> {
    public Optional<IPC> findByUserId(int id);
}
