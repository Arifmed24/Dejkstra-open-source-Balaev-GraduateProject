package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.RouteTimetables;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface RouteTimetablesRepository extends CrudRepository<RouteTimetables, Integer> {
    @Query()
    List<RouteTimetables> findByDateDepartureAfterAndDateArrivalBefore(@Temporal(TemporalType.TIMESTAMP) Date dateDeparture, @Temporal(TemporalType.TIMESTAMP) Date dateArrival);
}
