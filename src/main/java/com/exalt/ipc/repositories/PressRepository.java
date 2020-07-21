package com.exalt.ipc.repositories;

import com.exalt.ipc.entities.Press;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PressRepository extends CrudRepository<Press, Integer> {
	//		public Optional<Press> findByIpcId(int id);
}
