package com.exalt.ipc.repositories;

import com.exalt.ipc.entities.IPC;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPCRepository extends CrudRepository<IPC, Integer> {
	Optional<IPC> findByUserId(int id);

	Optional<IPC> findByPressId(int id);

}
