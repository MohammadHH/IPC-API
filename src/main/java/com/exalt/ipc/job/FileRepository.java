package com.exalt.ipc.job;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<File, Integer> {
    public List<File> findByState(String state);

    public List<File> findByStateNot(String state);

    public Optional<File> findByIdAndUserEmail(int id, String email);

    public List<File> findByStateAndUserId(String state, int id);

    List<File> findByUserId(int id);

//    List<File> findByIpcId(int id);
}
