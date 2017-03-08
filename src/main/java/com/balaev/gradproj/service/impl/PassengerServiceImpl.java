package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.domain.Passenger;
import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Ticket;
import com.balaev.gradproj.repository.PassengerRepository;
import com.balaev.gradproj.service.api.PassengerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("passengerService")
@Transactional
public class PassengerServiceImpl implements PassengerService {

    private static final Logger LOG = Logger.getLogger(PassengerServiceImpl.class);
    @Autowired
    PassengerRepository passengerRepository;

    @Override
    public boolean isExists(Passenger passenger) {
        Passenger passenger1 = passengerRepository.findByLastNameAndFirstNameAndBirth(passenger.getFirstName(), passenger.getLastName(), passenger.getBirth());
        if (passenger1 != null) {
            LOG.info("passenger exists");
            return true;
        } else {
            LOG.info("passenger doesn't exists");
            return false;
        }
    }

    @Override
    public Passenger create(Passenger passenger) {
        Passenger result;
        result = passengerRepository.save(passenger);
        LOG.info("created passenger {}", result);
        return result;
    }

    @Override
    public Passenger getByNameAndBirth(Passenger passenger) {
        Passenger result;
        result = passengerRepository.findByLastNameAndFirstNameAndBirth(passenger.getFirstName(), passenger.getLastName(), passenger.getBirth());
        LOG.info("passenger {} is found", result);
        return result;
    }

    @Override
    public Set<Passenger> getPassengersOfRoute(List<RouteTimetables> timetables) {
        Set<Passenger> result = new HashSet<>();
        for (RouteTimetables rt : timetables) {
            for (Ticket t : rt.getTickets()) {
                if (t.getTicketPassenger() != null) {
                    result.add(t.getTicketPassenger());
                }
            }
        }
        LOG.info("finding passengers of route");
        return result;
    }
}
