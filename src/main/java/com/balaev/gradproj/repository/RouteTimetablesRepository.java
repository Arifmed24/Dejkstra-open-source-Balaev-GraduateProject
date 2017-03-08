package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Route;
import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Station;
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


    List<RouteTimetables> findAll();

    @Query("SELECT rt FROM RouteTimetables rt INNER JOIN rt.line l WHERE l.stationArrival =:station AND rt.dateArrival between :date1 AND:date2")
    List<RouteTimetables> getStationTimetableArr(@Param("station") Station station, @Param("date1") Date dateBegin, @Param("date2") Date dateEnd);

    @Query("SELECT rt FROM RouteTimetables rt INNER JOIN rt.line l " +
            " WHERE l.stationDeparture =:station " +
            " AND rt.dateDeparture between :date1 AND:date2")
    List<RouteTimetables> getStationTimetableDep(@Param("station") Station station, @Param("date1") Date dateBegin, @Param("date2") Date dateEnd);

    @Query("SELECT r FROM RouteTimetables r order by r.numberInRoute")
    List<RouteTimetables> getRoutes();

    @Query("SELECT r FROM RouteTimetables r WHERE r.routeId =:route AND r.numberInRoute = :number "
            + "AND r.dateDeparture > :dateBegin "
            + "AND r.dateArrival < :dateEnd AND r.freeSeats > 0 order by r.dateDeparture")
    List<RouteTimetables> getRouteTimetableByRouteAndNumberInRoute(@Param("route") Route route, @Param("number") int number, @Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd);

    @Query("SELECT r FROM RouteTimetables r WHERE r.routeId =:route GROUP BY r.routeId, r.numberInRoute")
    List<RouteTimetables> getListRtByRoute(@Param("route") Route route);

    @Query("SELECT r FROM RouteTimetables r WHERE r.routeId =:route AND r.numberInRoute = :number "
            + "AND r.dateDeparture > :dateBegin "
            + "AND :dateEnd > r.dateArrival order by r.dateDeparture")
    List<RouteTimetables> getRoutesWithPassengers(@Param("route") Route route, @Param("number") int number, @Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd);

    @Query("SELECT r FROM RouteTimetables r WHERE r.dateDeparture > :dateBegin " +
            "AND :dateEnd < r.dateArrival")
    List<RouteTimetables> getRouteTimetablesInPeriod(@Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd);

}
