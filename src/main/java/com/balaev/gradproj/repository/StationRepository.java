package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Station;
import org.springframework.data.repository.CrudRepository;

public interface StationRepository extends CrudRepository<Station, Integer> {
    Station findBystationName(String stationName);
}
