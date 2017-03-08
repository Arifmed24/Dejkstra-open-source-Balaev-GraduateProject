package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.Route;
import com.balaev.gradproj.domain.Station;

import java.util.List;

public interface StationService {
    List<Station> getAllStations();

    Station read(int id);

    Station createStation(Station station);

    Station updateStation(Station station);

    List<Station> checkStationsInRoute(Route route, List<String> stationsId) throws Exception;

    Integer getNumberOfStations(List<Station> stations);
}
