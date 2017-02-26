package com.balaev.gradproj;

import com.balaev.gradproj.domain.Passenger;
import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.domain.User;
import com.balaev.gradproj.repository.PassengerRepository;
import com.balaev.gradproj.repository.RouteTimetablesRepository;
import com.balaev.gradproj.repository.StationRepository;
import com.balaev.gradproj.repository.UserRepository;
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
    private RouteTimetablesRepository routeTimetablesRepository;
    @Autowired
    private RouteTimetablesService routeTimetablesService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void loadUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        assertEquals("Arif Balaev", users.get(0).getFio());
    }

    @Test
    public void getPassengerByFullName() {
        Passenger passenger = passengerRepository.findByLastNameAndFirstName("Filippova", "Daria");
        assertEquals(2, (long) passenger.getIdPassenger());
    }

    @Test
    public void getStationByName() {
        Station station = stationRepository.findBystationName("Sestroretsk");
        assertEquals(4, (long) station.getIdStation());
    }

    @Test
    public void getRouteTimetablesBetweenTwoDates(){
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
        List<RouteTimetables> routeTimetables = routeTimetablesRepository.findByDateDepartureAfterAndDateArrivalBefore(startDate,finishDate);
        assertEquals(12, (long)routeTimetables.size());
    }

    @Test
    public void countStations() {
        Long count = stationRepository.count();
        assertEquals(10, (long) count);
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
        Station stationStart = stationRepository.findOne(1);
        Station stationFinish = stationRepository.findOne(5);
        String departure = "2016-10-01 00:00:00";
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
        routeTimetablesService.getShortestWay(stationStart, stationFinish, startDate, finishDate);
    }
}
