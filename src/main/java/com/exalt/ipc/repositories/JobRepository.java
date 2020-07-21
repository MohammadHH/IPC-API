package com.exalt.ipc.repositories;

import com.exalt.ipc.entities.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends CrudRepository<Job, Integer> {
	Optional<Job> findByIdAndUserEmail(int id, String email);

	List<Job> findByStateAndUserId(String state, int id);

	List<Job> findByUserId(int id);

}
