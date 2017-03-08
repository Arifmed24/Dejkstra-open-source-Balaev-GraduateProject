package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Train;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrainRepository extends CrudRepository<Train, Integer> {
    List<Train> findAll();
}
