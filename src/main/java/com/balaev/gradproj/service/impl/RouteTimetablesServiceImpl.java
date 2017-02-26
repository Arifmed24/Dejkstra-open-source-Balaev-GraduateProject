package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.repository.RouteTimetablesRepository;
import com.balaev.gradproj.service.api.RouteTimetablesService;
import com.balaev.gradproj.service.api.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("routeTimetablesService")
@Transactional
public class RouteTimetablesServiceImpl implements RouteTimetablesService {

    @Autowired
    RouteTimetablesRepository routeTimetablesRepository;
    @Autowired
    StationService stationService;

    private static int INF = Integer.MAX_VALUE / 2;

    private Map<Integer, ArrayList<Integer>> adj; // кто с кем контактирует (ключ - станция,
    private Map<Integer, Boolean> used; //массив для хранения информации о пройденных и не пройденных вершинах
    private Map<Integer, Double> dist; //массив для хранения расстояния от стартовой вершины
    private Map<Integer, ArrayList<RouteTimetables>> stationArrivingRoutes; //массив предков, необходимых для восстановления кратчайшего пути из стартовой вершины
    private Map<Integer, Integer> pred;
    private Map<Integer, Map<Integer, ArrayList<RouteTimetables>>> lines;
    int start; //стартовая вершина, от которой ищется расстояние до всех других


    List<Station> stations;
    List<RouteTimetables> routeTimetables;

    @Override
    public List<RouteTimetables> getShortestWay(Station startStation, Station finishStation, Date startDate, Date finishDate) {
        prepareData(startStation, startDate, finishDate);
        dejkstra(startStation.getIdStation(), startDate);
        return null;
    }

    void dejkstra(int s, Date startDate) {

        dist.put(s, (double) 0);
        RouteTimetables departureDate = new RouteTimetables();
        departureDate.setDateArrival(startDate);
        stationArrivingRoutes.put(s, new ArrayList<>(Collections.singletonList(departureDate)));


        for (int iter = 0; iter < stations.size(); ++iter) {
            int idCurrentStation = -1; //id current station
            Double distanceToCurrentStation = (double) INF; //dist to station

            //БЕРЕМ СТАНЦИЮ
            for (Station station : stations) {
                if (used.get(station.getIdStation())) //если использовалась - берем следующую
                    continue;
                if (distanceToCurrentStation < dist.get(station.getIdStation())) { //и до нее самое короткое расстояние из имеющихся
                    continue;
                }
                idCurrentStation = station.getIdStation(); // номер станции
                distanceToCurrentStation = dist.get(station.getIdStation()); //расстояние до неё
            }

            for (int i = 0; i < adj.get(idCurrentStation).size(); ++i) { //все смежные пути
                int connectedStationId = adj.get(idCurrentStation).get(i);
                //расстояние до вершины
                double weightU = lines.get(idCurrentStation).get(connectedStationId).get(0).getLine().getDistance();

                if (dist.get(idCurrentStation) + weightU < dist.get(connectedStationId)) { //если путь короче
                    for (RouteTimetables variant : lines.get(idCurrentStation).get(connectedStationId)) { //все варианты между станциями
                        for (RouteTimetables routeTimetableArriving : stationArrivingRoutes.get(idCurrentStation)) { // все варианты прибытия
                            if (routeTimetableArriving.getDateArrival().before(variant.getDateDeparture())) { // если дата выезда из А позже даты приезда в неё
                                ArrayList<RouteTimetables> previousRT = stationArrivingRoutes.get(connectedStationId);
                                if (previousRT.isEmpty()) {
                                    stationArrivingRoutes.put(connectedStationId, new ArrayList<>(Collections.singletonList(variant)));
                                } else {
                                    if (!previousRT.contains(variant)) {
                                        previousRT.add(variant);
                                        stationArrivingRoutes.put(connectedStationId, previousRT);
                                    }
                                }
                                pred.put(connectedStationId, idCurrentStation);
                                dist.put(connectedStationId, dist.get(idCurrentStation) + weightU);
                            }
                        }
                    }
                }
            }
            //помечаем вершину idCurrentStation просмотренной, до нее найдено кратчайшее расстояние
            used.put(idCurrentStation, true);
        }
    }

    /**
     * Prepare data for dejksta algorithm
     *
     * @param startStation station departure
     * @param startDate    date departure
     * @param finishDate   date arrival
     */
    void prepareData(Station startStation, Date startDate, Date finishDate) {
        stations = stationService.getAllStations();
        routeTimetables = getRouteTimetablesInPeriod(startDate, finishDate);

        start = startStation.getIdStation();

        //initializing Maps
        adj = new HashMap<>();
        lines = new HashMap<>();
        used = new HashMap<>();
        stationArrivingRoutes = new HashMap<>();
        dist = new HashMap<>();
        pred = new HashMap<>();

        //fill default
        for (Station station : stations) {
            adj.put(station.getIdStation(), new ArrayList<>());
            lines.put(station.getIdStation(), new HashMap<>());
            used.put(station.getIdStation(), false);
            dist.put(station.getIdStation(), (double) INF);
            stationArrivingRoutes.put(station.getIdStation(), new ArrayList<>());
            pred.put(station.getIdStation(), -1);
        }

        //fill adj and lines
        for (RouteTimetables routeTimetable : routeTimetables) {


            //берем список смежностей станции отправки этого отрезка
            ArrayList<Integer> connnectedStations = adj.get(routeTimetable.getLine().getStationDeparture().getIdStation());
            //если список пуст
            if (connnectedStations.size() == 0) {
                //добавляем станцию прибытия в список смежностей
                adj.put(routeTimetable.getLine().getStationDeparture().getIdStation(), new ArrayList<>(Collections.singletonList(routeTimetable.getLine().getStationArrival().getIdStation())));
                //добавляем отрезок к станции прибытия
                lines.put(routeTimetable.getLine().getStationDeparture().getIdStation(), new HashMap<>(Collections.singletonMap(routeTimetable.getLine().getStationArrival().getIdStation(), new ArrayList<>(Collections.singletonList(routeTimetable)))));
                //если список не пуст
            } else {
                //если станции прибытия еще нет в списке
                if (!connnectedStations.contains(routeTimetable.getLine().getStationArrival().getIdStation())) {
                    //добавляем ее в конец списка
                    connnectedStations.add(routeTimetable.getLine().getStationArrival().getIdStation());
                    adj.put(routeTimetable.getLine().getStationDeparture().getIdStation(), connnectedStations);
                }
                //берем все отрезки выходящие из станции отправки
                Map<Integer, ArrayList<RouteTimetables>> currentLines = lines.get(routeTimetable.getLine().getStationDeparture().getIdStation());
                //берем отрезки приходящие только в станцию прибытия
                ArrayList<RouteTimetables> listOfTimetable = currentLines.get(routeTimetable.getLine().getStationArrival().getIdStation());
                //если список пуст
                if (listOfTimetable == null) {
                    //добавляем отрезок к станции прибытия
                    listOfTimetable = new ArrayList<>(Collections.singletonList(routeTimetable));
                    //если список не пуст
                } else {
                    //добавляем еще одно расписание отрезка
                    listOfTimetable.add(routeTimetable);
                }

                //обновляем список отрезков у промежутка станции отправки и станции прибытия
                currentLines.put(routeTimetable.getLine().getStationArrival().getIdStation(), listOfTimetable);
                lines.put(routeTimetable.getLine().getStationDeparture().getIdStation(), currentLines);
            }
        }

    }

    List<RouteTimetables> getRouteTimetablesInPeriod(Date startDate, Date finishDate) {
        return routeTimetablesRepository.findByDateDepartureAfterAndDateArrivalBefore(startDate, finishDate);
    }
}
