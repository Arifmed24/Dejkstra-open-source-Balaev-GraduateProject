package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Passenger;
import org.springframework.data.repository.CrudRepository;

public interface PassengerRepository extends CrudRepository<Passenger, Integer> {
    Passenger findByLastNameAndFirstName(String lastName, String firstName);
}
