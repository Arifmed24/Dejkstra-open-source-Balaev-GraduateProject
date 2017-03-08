package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.Passenger;
import com.balaev.gradproj.domain.RouteTimetables;

import java.util.List;
import java.util.Set;

public interface PassengerService {
    boolean isExists(Passenger passenger);

    Passenger create(Passenger passenger);

    Passenger getByNameAndBirth(Passenger passenger);

    Set<Passenger> getPassengersOfRoute(List<RouteTimetables> timetables);
}
