package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Station;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StationRepository extends CrudRepository<Station, Integer> {
    /**
     * Find station by it's name
     *
     * @param stationName name of station
     * @return Station entity
     */
    Station findBystationName(String stationName);

    List<Station> findAll();
}
