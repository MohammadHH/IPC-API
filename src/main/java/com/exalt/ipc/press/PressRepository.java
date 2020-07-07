package com.exalt.ipc.press;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PressRepository extends CrudRepository<Press, Integer> {
}
