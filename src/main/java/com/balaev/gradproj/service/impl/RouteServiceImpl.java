package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.domain.Route;
import com.balaev.gradproj.repository.RouteRepository;
import com.balaev.gradproj.service.api.RouteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("routeService")
@Transactional
public class RouteServiceImpl implements RouteService {
    private static final Logger LOG = Logger.getLogger(RouteServiceImpl.class);
    @Autowired
    RouteRepository routeRepository;

    @Override
    public Route createRoute(Route route) {
        Route result;
        result = routeRepository.save(route);
        LOG.info("route created {}", result);
        return result;
    }

    @Override
    public List<Route> getAllRoutes() {
        LOG.info("get all routes");
        return routeRepository.findAll();
    }

    @Override
    public Route readRoute(int id) {
        LOG.info("read route");
        return routeRepository.findOne(id);
    }
}
