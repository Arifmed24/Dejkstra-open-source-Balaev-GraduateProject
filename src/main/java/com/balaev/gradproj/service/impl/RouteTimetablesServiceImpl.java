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

    private int countStations; //количество вершин в орграфе
    private int countRouteTimetables; //количествое дуг в орграфе
    private Map<Integer, ArrayList<Integer>> adj; // кто с кем контактирует (ключ - станция,
    //    private Map<Integer, Map<Integer, Double>> length; //КИЛОМЕТРАЖ СООТЕТСТВУЮЩЕЙ СМЕЖНОСТИ
//    private Map<Integer, Map<Integer, ArrayList<ArrayList<Date>>>> dateArriving; //ДАТА ПРИБЫТИЯ
    private Map<Integer, Boolean> used; //массив для хранения информации о пройденных и не пройденных вершинах
    private Map<Integer, Double> dist; //массив для хранения расстояния от стартовой вершины
    //массив предков, необходимых для восстановления кратчайшего пути из стартовой вершины
    private Map<Integer, Integer> pred;
    private Map<Integer, Map<Integer, ArrayList<RouteTimetables>>> lines;
    int start; //стартовая вершина, от которой ищется расстояние до всех других

    @Override
    public List<RouteTimetables> getShortestWay(Station startStation, Station finishStation, Date startDate, Date finishDate) {
        prepareData(startStation, startDate, finishDate);
        return null;
    }


    //найти самый короткий путь
    //процедура запуска алгоритма Дейкстры из стартовой вершины
//    private void dejkstra(int s) {
//        dist[s] = 0; //кратчайшее расстояние до стартовой вершины равно 0
//        for (int iter = 0; iter < countStations; ++iter) {
//            int v = -1;
//            int distV = INF;
//            //выбираем вершину, кратчайшее расстояние до которого еще не найдено
//            for (int i = 0; i < countStations; ++i) {
//                if (used[i]) {
//                    continue;
//                }
//                if (distV < dist[i]) {
//                    continue;
//                }
//                v = i; //станция
//                distV = dist[i]; //расстояние до станции
//            }
//            //рассматриваем все дуги, исходящие из найденной вершины
//            for (int i = 0; i < adj[v].size(); ++i) {
//                int u = adj[v].get(i); //станция, с которой связана
//                int weightU = length[v].get(i); //вес
//                // TODO: 15.02.17 добавить время (прибытия)
//                //релаксация вершины
//                if (dist[v] + weightU < dist[u]) { //+время из вершины
//                    dist[u] = dist[v] + weightU;
//                    pred[u] = v;
//                }
//            }
//            //помечаем вершину v просмотренной, до нее найдено кратчайшее расстояние
//            used[v] = true;
//        }
//    }


    void prepareData(Station startStation, Date startDate, Date finishDate) {
        List<Station> stations = stationService.getAllStations();
        List<RouteTimetables> routeTimetables = getRouteTimetablesInPeriod(startDate, finishDate);

        start = startStation.getIdStation();

        //initializing Maps
        adj = new HashMap<>();
        lines = new HashMap<>();
        used = new HashMap<>();
        pred = new HashMap<>();
        dist = new HashMap<>();

        //fill default
        for (Station station : stations) {
            adj.put(station.getIdStation(), new ArrayList<>());
            lines.put(station.getIdStation(), new HashMap<>());
            used.put(station.getIdStation(), false);
            pred.put(station.getIdStation(), -1);
            dist.put(station.getIdStation(), (double) INF);
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
