package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.RouteTimetables;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface RouteTimetablesRepository extends CrudRepository<RouteTimetables, Integer> {
    /**
     * Find all Route Timetables that are correct for datetime period
     *
     * @param dateDeparture begining date and time of finding period
     * @param dateArrival   ending date and time of finding period
     * @return list of RouteTimetables that are correct for this period
     */
    List<RouteTimetables> findByDateDepartureAfterAndDateArrivalBefore(@Temporal(TemporalType.TIMESTAMP) Date dateDeparture, @Temporal(TemporalType.TIMESTAMP) Date dateArrival);

    /**
     * Get count of unique timetables that are correct for datetime period
     *
     * @param dateDeparture begining date and time of finding period
     * @param dateArrival   ending date and time of finding period
     * @return list of Timetables and their count
     */
    @Query("select tr.line, count(tr) from RouteTimetables tr where tr.dateDeparture > :dateDeparture and tr.dateArrival < :dateArrival group by tr.line")
    List<RouteTimetables> findByDateDepartureAfterAndDateArrivalBeforeOrderByLine(@Temporal(TemporalType.TIMESTAMP) @Param("dateDeparture") Date dateDeparture, @Temporal(TemporalType.TIMESTAMP) @Param("dateArrival") Date dateArrival);
}
