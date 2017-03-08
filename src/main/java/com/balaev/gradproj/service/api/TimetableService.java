package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.domain.Timetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TimetableService {
    Timetable readByStations(Station stationBegin, Station stationEnd) throws Exception;

    Set<Timetable> getTimetableListFromRouteTimetables(List<RouteTimetables> routeTimetablesList);

    ArrayList getRelatedStations(List<Station> stations, Set<Timetable> timetables, int numberOfStations);
}
