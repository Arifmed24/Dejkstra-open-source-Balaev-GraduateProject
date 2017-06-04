package com.balaev.gradproj;

import com.balaev.gradproj.domain.*;
import com.balaev.gradproj.repository.*;
import com.balaev.gradproj.service.api.RouteTimetablesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BalaevGraduateProjectApplication.class)
@WebAppConfiguration
public class BalaevGraduateProjectApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TimetableRepository timetableRepository;
    @Autowired
    private RouteTimetablesRepository routeTimetablesRepository;
    @Autowired
    private RouteTimetablesService routeTimetablesService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void getPassengerByFullName() {

        String birth_s = "1995-03-08";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = null;
        try {
            birth = dateFormat.parse(birth_s);
        } catch (ParseException e) {
            System.out.println("ERROR IN PARSING");
            e.printStackTrace();
        }
        Passenger passenger = passengerRepository.findByLastNameAndFirstNameAndBirth("Filippova", "Daria", birth);
        assertEquals(2, (long) passenger.getIdPassenger());
    }

    @Test
    public void getStationByName() {
        Station station = stationRepository.findBystationName("Sestroretsk");
        assertEquals(4, (long) station.getIdStation());
    }

    @Test
    public void getRouteTimetablesBetweenTwoDates() {
        String departure = "2016-11-01 00:00:00";
        String arrival = "2016-11-30 00:00:00";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startDate = null;
        Date finishDate = null;
        try {
            startDate = dateFormat.parse(departure);
            finishDate = dateFormat.parse(arrival);
        } catch (ParseException e) {
            System.out.println("ERROR IN PARSING");
            e.printStackTrace();
        }
        List<RouteTimetables> routeTimetables = routeTimetablesRepository.findByDateDepartureAfterAndDateArrivalBefore(startDate, finishDate);
        assertEquals(12, (long) routeTimetables.size());
    }

    @Test
    public void countStations() {
        Long count = stationRepository.count();
        assertTrue(count > 10);
    }

    @Test
    public void countRouteTimetables() {
        String departure = "2016-11-01 00:00:00";
        String arrival = "2016-11-30 00:00:00";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startDate = null;
        Date finishDate = null;
        try {
            startDate = dateFormat.parse(departure);
            finishDate = dateFormat.parse(arrival);
        } catch (ParseException e) {
            System.out.println("ERROR IN PARSING");
            e.printStackTrace();
        }
        List<RouteTimetables> routeTimetables = routeTimetablesRepository.findByDateDepartureAfterAndDateArrivalBeforeOrderByLine(startDate, finishDate);
        assertEquals(6, (long) routeTimetables.size());
    }

    @Test
    public void getShortestWay() throws Exception {
        Station stationStart = stationRepository.findOne(2);
        Station stationFinish = stationRepository.findOne(4);
        String departure = "2017-07-01 00:00:00";
        String arrival = "2017-07-15 00:00:00";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startDate = null;
        Date finishDate = null;
        try {
            startDate = dateFormat.parse(departure);
            finishDate = dateFormat.parse(arrival);
        } catch (ParseException e) {
            System.out.println("ERROR IN PARSING");
            e.printStackTrace();
        }
        routeTimetablesService.getShortestWay(stationStart, stationFinish, startDate, finishDate);
    }

    @Test
    public void getAllRoutes() throws Exception {
        List<Route> routes = routeRepository.findAll();
        assertEquals(8, routes.size());
    }

    @Test
    public void getTimetabeByStations() {
        Station begin = stationRepository.findBystationName("Pavlovsk");
        Station end = stationRepository.findBystationName("Pupuishevo");
        Timetable result = timetableRepository.readByStations(begin, end);
        assertEquals(5, (long) result.getIdLine());
    }

}
