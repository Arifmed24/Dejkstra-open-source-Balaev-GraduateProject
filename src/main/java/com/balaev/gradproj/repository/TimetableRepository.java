package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.domain.Timetable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TimetableRepository extends CrudRepository<Timetable, Integer> {
    @Query("SELECT t FROM Timetable t WHERE t.stationDeparture = :stationBegin AND t.stationArrival = :stationEnd")
    Timetable readByStations(@Param("stationBegin") Station stationBegin, @Param("stationEnd") Station stationEnd);
}
