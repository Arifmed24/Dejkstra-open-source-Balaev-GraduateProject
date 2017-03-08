package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Passenger;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface PassengerRepository extends CrudRepository<Passenger, Integer> {
    Passenger findByLastNameAndFirstNameAndBirth(String lastName, String firstName, Date birth);
}
