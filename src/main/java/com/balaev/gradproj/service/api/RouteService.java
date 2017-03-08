package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.Route;

import java.util.List;

public interface RouteService {
    Route createRoute(Route route);

    List<Route> getAllRoutes();

    Route readRoute(int id);
}
