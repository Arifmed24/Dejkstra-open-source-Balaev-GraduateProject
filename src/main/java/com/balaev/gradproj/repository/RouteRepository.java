package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RouteRepository extends CrudRepository<Route,Integer> {
    List<Route> findAll();
}
