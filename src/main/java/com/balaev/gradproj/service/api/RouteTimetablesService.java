package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Station;

import java.util.Date;
import java.util.List;

public interface RouteTimetablesService {
    List<RouteTimetables> getShortestWay(Station startStation, Station finishStation, Date startDate, Date finishDate);

}
