package com.exalt.ipc.press;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PressRepository extends CrudRepository<Press, Integer> {
    public Optional<Press> findByIdAndUserEmail(int id, String email);

//    public Optional<Press> findByUserEmailAndMappedTrue(String email);

}
