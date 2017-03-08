package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.domain.Route;
import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.repository.StationRepository;
import com.balaev.gradproj.service.api.StationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("stationService")
@Transactional
public class StationServiceImpl implements StationService {

    private static final Logger LOG = Logger.getLogger(StationServiceImpl.class);

    @Autowired
    StationRepository stationRepository;

    @Override
    public List<Station> getAllStations() {
        return (List<Station>) stationRepository.findAll();
    }

    @Override
    public Station read(int id) {
        Station result;
        result = stationRepository.findOne(id);
        return result;
    }

    @Override
    public Station createStation(Station station) {
        Station result;
        result = stationRepository.save(station);
        LOG.info("station created {}", result);
        return result;
    }

    @Override
    public Station updateStation(Station station) {
        Station result;
        result = stationRepository.save(station);
        LOG.info("station updated {}", result);
        return result;
    }

    @Override
    public List<Station> checkStationsInRoute(Route route, List<String> stationsId) throws Exception {
        LOG.info("start checking stations in route");
        List<Station> result = new ArrayList<>();
        result.add(route.getStartStation());
        result.add(route.getFinishStation());
        for (String stationId : stationsId) {
            int idStation = Integer.parseInt(stationId);
            Station nextStation = stationRepository.findOne(idStation);
            if (!result.contains(nextStation)) {
                result.add(result.size() - 1, nextStation);
            } else {
                LOG.error("Route has one station more than one time");
                throw new Exception("Route can't have one station twice");
            }
        }
        LOG.info("finish checking stations in route");
        return result;
    }

    @Override
    public Integer getNumberOfStations(List<Station> stations) {
        return stations.size();
    }
}
