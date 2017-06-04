package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.domain.Route;
import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.repository.RouteRepository;
import com.balaev.gradproj.repository.RouteTimetablesRepository;
import com.balaev.gradproj.service.api.RouteTimetablesService;
import com.balaev.gradproj.service.api.StationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("routeTimetablesService")
@Transactional
public class RouteTimetablesServiceImpl implements RouteTimetablesService {

    private static final Logger LOG = Logger.getLogger(RouteTimetablesServiceImpl.class);
    //millisecond in minute
    private static final int MILLIS_IN_MIN = 60000;

    @Autowired
    RouteTimetablesRepository routeTimetablesRepository;
    @Autowired
    RouteRepository routeRepository;
    @Autowired
    StationService stationService;

    private static int INF = Integer.MAX_VALUE / 2;

    private Map<Integer, ArrayList<Integer>> adj; // кто с кем контактирует (ключ - станция, массив - контакты)
    private Map<Integer, Boolean> used; //массив для хранения информации о пройденных и не пройденных вершинах
    private Map<Integer, Double> dist; //массив для хранения расстояния от стартовой вершины
    private Map<Integer, Long> duration;//массив для хранения затраченного времени от стартовой вершины
    private Map<Integer, ArrayList<RouteTimetables>> stationArrivingRoutes; //отрезки, чьих концовкой является данная станция
    private Map<Integer, Integer> pred; //хранение предыдущей вершины
    private Map<Integer, Map<Integer, ArrayList<RouteTimetables>>> lines; //отрезки между двумя станциями

    private Station stationBegin;
    private List<Station> stations;
    private List<List<RouteTimetables>> ways;

    @Override
    public List<List<RouteTimetables>> getShortestWay(Station startStation, Station finishStation, Date startDate, Date finishDate) {
        stationBegin = startStation;
        prepareData(startDate, finishDate);
        dejkstra(startStation.getIdStation(), startDate);
//        dejkstraByTime(startStation.getIdStation(), startDate);
        return getWays(finishStation);
    }

    List<List<RouteTimetables>> getWays(Station finishStation) {
        ways = new ArrayList<>();
        prepareListOfVariants(finishStation.getIdStation());
        deleteSimilarVariants();
        return ways;
    }

    private void prepareListOfVariants(int station) {
        for (int child = 0; child < stationArrivingRoutes.get(station).size(); child++) {
            if (stationArrivingRoutes.get(station).get(child).getLine().getStationDeparture().getIdStation() != stationBegin.getIdStation()) {
                prepareListOfVariants(stationArrivingRoutes.get(station).get(child).getLine().getStationDeparture().getIdStation());
                fill(stationArrivingRoutes.get(station).get(child));
            } else {
                ways.add(new ArrayList<>(Collections.singletonList(stationArrivingRoutes.get(station).get(child))));
            }
        }
    }

    private void deleteSimilarVariants() {
        Set<List<RouteTimetables>> hs = new HashSet<>();
        hs.addAll(ways);
        ways.clear();
        ways.addAll(hs);
    }

    private void fill(RouteTimetables nextRoutetimetable) {
        for (List<RouteTimetables> way : ways) {
            if (Objects.equals(way.get(way.size() - 1).getLine().getStationArrival().getIdStation(), nextRoutetimetable.getLine().getStationDeparture().getIdStation()) && way.get(way.size() - 1).getDateArrival().before(nextRoutetimetable.getDateDeparture()))
                way.add(nextRoutetimetable);
        }
    }

    @Override
    public RouteTimetables createRoutetimetable(RouteTimetables routeTimetables) {
        RouteTimetables result;
        result = routeTimetablesRepository.save(routeTimetables);
        return result;
    }

    private Date[] formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datetime1 = sdf.format(date) + " 00:00:00";
        String datetime2 = sdf.format(date) + " 23:59:59";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = formatter.parse(datetime1);
            date2 = formatter.parse(datetime2);
        } catch (ParseException e) {
            e.printStackTrace();
            LOG.error("Error with date parsing ", e);
        }
        return new Date[]{date1, date2};
    }

    @Override
    public List<RouteTimetables> getTimetableStationArr(Station station, Date date) {
        Date[] dates = formatDate(date);
        Date date1 = dates[0];
        Date date2 = dates[1];
        LOG.info("getting station timetable for arriving");
        return routeTimetablesRepository.getStationTimetableArr(station, date1, date2);
    }

    @Override
    public List<RouteTimetables> getTimetableStationDep(Station station, Date date) {
        Date[] dates = formatDate(date);
        Date date1 = dates[0];
        Date date2 = dates[1];
        LOG.info("getting station timetable for departure");
        return routeTimetablesRepository.getStationTimetableDep(station, date1, date2);
    }

    @Override
    public Map<Integer, List<Integer>> getRoutes() {
        LOG.info("start getting all routes");
        List<RouteTimetables> allRoutes = routeTimetablesRepository.getRoutes();
        Map<Integer, List<Integer>> routes = new HashMap<>();
        for (RouteTimetables r : allRoutes) {
            int idRoute = r.getRouteId().getIdRoute();
            if (!routes.containsKey(idRoute)) {
                List<Integer> route = new ArrayList<>();
                route.add(r.getLine().getStationDeparture().getIdStation());
                route.add(r.getLine().getStationArrival().getIdStation());
                routes.put(idRoute, route);
            } else {
                List<Integer> myRoute = routes.get(idRoute);
                if (!myRoute.contains(r.getLine().getStationArrival().getIdStation())) {
                    routes.get(idRoute).add(r.getLine().getStationArrival().getIdStation());
                }
            }
        }
        LOG.info("finish getting all routes");
        return routes;
    }

    @Override
    public RouteTimetables updateRouteTimetable(RouteTimetables routeTimetables) {
        RouteTimetables result;
        result = routeTimetablesRepository.save(routeTimetables);
        LOG.info("timetable {} updated", result);
        return result;
    }

    @Override
    public List<RouteTimetables> createTemplateOfGraphic(Route route) {
        LOG.info("start creating template for new graphic");
        List<RouteTimetables> template = routeTimetablesRepository.getListRtByRoute(route);
        List<RouteTimetables> graphicTemplate = new ArrayList<>();
        for (RouteTimetables r : template) {
            RouteTimetables routeTimetables = new RouteTimetables();
            routeTimetables.setLine(r.getLine());
            routeTimetables.setDateDeparture(new Date());
            routeTimetables.setDateArrival(new Date());
            routeTimetables.setFreeSeats(route.getTrain().getSeats());
            routeTimetables.setRouteId(route);
            routeTimetables.setNumberInRoute(r.getNumberInRoute());
            graphicTemplate.add(routeTimetables);
        }
        LOG.info("finish creating template for new graphic");
        return graphicTemplate;
    }

    @Override
    public List<RouteTimetables> addDateInGraphic(List<Date> dates, Route route) throws Exception {
        LOG.info("start adding dates in graphic template");
        List<RouteTimetables> finalList = createTemplateOfGraphic(route);
        for (int i = 1; i < dates.size(); i++) {
            if (dates.get(i).before(dates.get(i - 1))) {
                LOG.error("dates are not correct");
                throw new Exception("Date arrival must be after date departure and earlier than next date departure");
            }
        }
        for (int i = 0; i < finalList.size(); i++) {
            finalList.get(i).setDateDeparture(dates.get(i * 2));
            finalList.get(i).setDateArrival(dates.get(i * 2 + 1));
        }
        LOG.info("finish adding dates in graphic template");
        return finalList;
    }

    @Override
    public List<RouteTimetables> createGraphic(List<RouteTimetables> routeTimetables) {
        LOG.info("start creating new graphic");
        List<RouteTimetables> result = new ArrayList<>();
        for (RouteTimetables r : routeTimetables) {
            RouteTimetables newRouteTimetable = new RouteTimetables();
            newRouteTimetable.setDateDeparture(r.getDateDeparture());
            newRouteTimetable.setDateArrival(r.getDateArrival());
            newRouteTimetable.setFreeSeats(r.getFreeSeats());
            newRouteTimetable.setLine(r.getLine());
            newRouteTimetable.setRouteId(r.getRouteId());
            newRouteTimetable.setNumberInRoute(r.getNumberInRoute());
            newRouteTimetable = createRoutetimetable(newRouteTimetable);
            result.add(newRouteTimetable);
        }
        LOG.info("finish creating new graphic");
        return result;
    }

    @Override
    public List<Date> checkDatesInRoute(List<String> dates) throws Exception {
        LOG.info("start checking dates of route");
        List<Date> routeDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (String sDate : dates) {
            Date date;
            try {
                date = sdf.parse(sDate);
                routeDates.add(date);
            } catch (ParseException e) {
                LOG.error("wrong format of date");
                throw new Exception("Some date was incorrect, please use calendar");
            }
        }
        for (int i = 1; i < routeDates.size(); i++) {
            if (routeDates.get(i).before(routeDates.get(i - 1))) {
                LOG.error("dates are not correct");
                throw new Exception("Date arrival must be after date departure and earlier than next date departure");
            }
        }
        LOG.info("finish checking dates of route");
        return routeDates;
    }

    @Override
    public List<List<RouteTimetables>> findWay2(Station stationBegin, Station stationEnd, Date dateBegin, Date dateEnd) {
        List<List<RouteTimetables>> result = new ArrayList<>();
        Map<Integer, List<Integer>> routes = this.getRoutes();
        int stationBeginId = stationBegin.getIdStation();
        int stationEndId = stationEnd.getIdStation();
        //first segments in variants
        List<RouteTimetables> startLines = null;
        int size = result.size();
        LOG.info("finding variants of ways");
        //take a map of all routes
        for (Map.Entry<Integer, List<Integer>> entry : routes.entrySet()) {
            //flags
            int start = -1, finish = -1;
            //indexes of stations in route
            int sj = -1, fj = -1;
            //go through each of route
            for (int i = 0; i < entry.getValue().size(); i++) {
                //if station in route equals stationBegin, record index of station to sj
                if (stationBeginId == entry.getValue().get(i)) {
                    start = entry.getValue().get(i);
                    sj = i;
                }
                //if station in route equals stationEnd, record index of station to fj
                if (stationEndId == entry.getValue().get(i)) {
                    finish = entry.getValue().get(i);
                    fj = i;
                }
            }
            //if route have this stations and start station stays earlier than finish station
            if (start != -1 && finish != -1 && sj < fj) {
                //take all segments (timetables) in route between start and finish stations
                for (int i = sj + 1; i < fj + 1; i++) {
                    //if this first segment
                    if (i == sj + 1) {
                        //take all first segments in all graphics of this route
                        startLines = routeTimetablesRepository.getRoutesWithPassengers(routeRepository.findOne(entry.getKey()), i, dateBegin, dateEnd);
                        //each of segments put in new array list (future result)
                        for (RouteTimetables rt : startLines) {
                            List<RouteTimetables> way = new ArrayList<>();
                            way.add(rt);
                            LOG.info("variant created");
                            result.add(way);
                        }
                        //if this not first segment
                    } else {
                        if (startLines.size() != 0) {
                            List<RouteTimetables> nextLines;
                            //take all segments with this index in all graphics of this route
                            nextLines = routeTimetablesRepository.getRoutesWithPassengers(routeRepository.findOne(entry.getKey()), i, dateBegin, dateEnd);
                            //go through all segments (this segment is determined number in route - routeTimetable
                            for (RouteTimetables line : nextLines) {
                                //go through variants
                                for (List<RouteTimetables> list : result) {
                                    //if variant doesn't have this segment
                                    if (line.getNumberInRoute() - list.get(list.size() - 1).getNumberInRoute() == 1) {
                                        //if date arrival of this segment before date arrival of last segment in variant
                                        if (line.getDateDeparture().after(list.get(list.size() - 1).getDateDeparture())) {
                                            if (list.get(list.size() - 1).getLine().getStationArrival() != line.getLine().getStationArrival())
                                                //add in variant
                                                list.add(line);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //if result size has changed
                if (result.size() != size) {
                    //check variants
                    int count = fj - sj;
                    //go through variants
                    for (int i = result.size(); i > size; i--) {
                        List<RouteTimetables> variant = result.get(i - 1);
                        if (variant.size() != count) {
                            LOG.info("variant was incorrect and deleted");
                            //remove this variant
                            result.remove(i - 1);
                        } else {
                            LOG.info("variant is correct");
                        }
                    }
                }
                size = result.size();
            }
        }
        LOG.info("finished finding");
        return result;
    }

    /**
     * Finds shortest way with time variants
     *
     * @param s         id of start station
     * @param startDate begining date of finding way
     */
    private void dejkstra(int s, Date startDate) {

        dist.put(s, (double) 0);
        RouteTimetables departureDate = new RouteTimetables();
        departureDate.setDateArrival(startDate);
        stationArrivingRoutes.put(s, new ArrayList<>(Collections.singletonList(departureDate)));

        for (int iter = 0; iter < stations.size(); ++iter) {
            int idCurrentStation = -1; //id current station
            Double distanceToCurrentStation = (double) INF; //dist to station
            for (Station station : stations) { //БЕРЕМ СТАНЦИЮ
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


    private void initPropertties() {
        adj = new HashMap<>();
        lines = new HashMap<>();
        used = new HashMap<>();
        stationArrivingRoutes = new HashMap<>();
        dist = new HashMap<>();
        duration = new HashMap<>();
        pred = new HashMap<>();
    }

    private void fillPropertiesWithDefaultValues() {
        for (Station station : stations) {
            adj.put(station.getIdStation(), new ArrayList<>());
            lines.put(station.getIdStation(), new HashMap<>());
            used.put(station.getIdStation(), false);
            dist.put(station.getIdStation(), (double) INF);
            duration.put(station.getIdStation(), (long) INF);
            stationArrivingRoutes.put(station.getIdStation(), new ArrayList<>());
            pred.put(station.getIdStation(), -1);
        }
    }

    private ArrayList<Integer> getConnectedStationsOfDepartureStationInTimtable(RouteTimetables routeTimetable) {
        return adj.get(routeTimetable.getLine().getStationDeparture().getIdStation());
    }

    private Integer getDepartureOfTimetable(RouteTimetables routeTimetable) {
        return routeTimetable.getLine().getStationDeparture().getIdStation();
    }

    private Integer getArrivalOfTimetable(RouteTimetables routeTimetable) {
        return routeTimetable.getLine().getStationArrival().getIdStation();
    }

    private void addStationArrivalToAdj(RouteTimetables routeTimetable) {
        //добавляем станцию прибытия в список смежностей
        adj.put(getDepartureOfTimetable(routeTimetable), new ArrayList<>(Collections.singletonList(getArrivalOfTimetable(routeTimetable))));
        //добавляем отрезок к станции прибытия
        lines.put(getDepartureOfTimetable(routeTimetable), new HashMap<>(Collections.singletonMap(getArrivalOfTimetable(routeTimetable), new ArrayList<>(Collections.singletonList(routeTimetable)))));
    }

    private void checkAndAddArrivalStationToAdj(ArrayList<Integer> stations, RouteTimetables routeTimetable) {
        if (!stations.contains(getArrivalOfTimetable(routeTimetable))) {
            //добавляем ее в конец списка
            stations.add(getArrivalOfTimetable(routeTimetable));
            adj.put(getDepartureOfTimetable(routeTimetable), stations);
        }
    }

    private void addRouteTimetableToLines(RouteTimetables routeTimetable) {
        //берем все отрезки выходящие из станции отправки
        Map<Integer, ArrayList<RouteTimetables>> linesFromStationDeparture = lines.get(getDepartureOfTimetable(routeTimetable));

        //берем отрезки приходящие только в станцию прибытия
        ArrayList<RouteTimetables> linesBetweenTwoStations = linesFromStationDeparture.get(getArrivalOfTimetable(routeTimetable));

        if (linesBetweenTwoStations == null) {
            //добавляем отрезок к станции прибытия
            linesBetweenTwoStations = new ArrayList<>(Collections.singletonList(routeTimetable));
            //если список не пуст
        } else {
            //добавляем еще одно расписание отрезка
            linesBetweenTwoStations.add(routeTimetable);
        }

        //обновляем список отрезков у промежутка станции отправки и станции прибытия
        linesFromStationDeparture.put(getArrivalOfTimetable(routeTimetable), linesBetweenTwoStations);
        lines.put(getDepartureOfTimetable(routeTimetable), linesFromStationDeparture);
    }

    /**
     * Prepare data for dejksta algorithm
     *
     * @param startDate  date departure
     * @param finishDate date arrival
     */
    private void prepareData(Date startDate, Date finishDate) {
        stations = stationService.getAllStations();
        List<RouteTimetables> routeTimetables = getRouteTimetablesInPeriod(startDate, finishDate);

        initPropertties();

        fillPropertiesWithDefaultValues();

        //fill adj and lines
        for (RouteTimetables routeTimetable : routeTimetables) {

            //берем список смежностей станции отправки этого отрезка
            ArrayList<Integer> connnectedStations = getConnectedStationsOfDepartureStationInTimtable(routeTimetable);

            //если список пуст
            if (connnectedStations.size() == 0) {

                addStationArrivalToAdj(routeTimetable);

            } else {

                //если станции прибытия еще нет в списке
                checkAndAddArrivalStationToAdj(connnectedStations, routeTimetable);

                addRouteTimetableToLines(routeTimetable);
            }
        }
    }

    public List<RouteTimetables> getRouteTimetablesInPeriod(Date startDate, Date finishDate) {
        return routeTimetablesRepository.findByDateDepartureAfterAndDateArrivalBefore(startDate, finishDate);
    }

    private void dejkstraByTime(int s, Date startDate) {

        duration.put(s, (long) 0); //расстояние до начальной станции = 0
        RouteTimetables departureDate = new RouteTimetables();
        departureDate.setDateArrival(startDate);
        stationArrivingRoutes.put(s, new ArrayList<>(Collections.singletonList(departureDate)));

        //идём по всем станциям
        for (int iter = 0; iter < stations.size(); ++iter) {
            //дефолтные настройки
            int idCurrentStation = -1;
            Long durationToCurrentStation = (long) INF;

            //БЕРЕМ СТАНЦИЮ
            for (Station station : stations) {
                //если использовалась - пропускаем
                if (used.get(station.getIdStation()))
                    continue;
                //если до нее быстрее, чем до остальных
                if (durationToCurrentStation < duration.get(station.getIdStation())) {
                    continue;
                }
                idCurrentStation = station.getIdStation(); // номер станции
                durationToCurrentStation = duration.get(station.getIdStation()); //время до неё
            }

            //все смежные пути
            for (int i = 0; i < adj.get(idCurrentStation).size(); ++i) {
                //берем смежную станцию
                int connectedStationId = adj.get(idCurrentStation).get(i);
                //проходимся по всем вариантам между двумя вершинами
                for (int v = 0; v < lines.get(idCurrentStation).get(connectedStationId).size(); v++) {

                    // все варианты прибытия
                    for (RouteTimetables routeTimetableArriving : stationArrivingRoutes.get(idCurrentStation)) {
                        //время в пути между вершинами
                        long weightU = lines.get(idCurrentStation).get(connectedStationId).get(v).getDateArrival().getTime() -
                                lines.get(idCurrentStation).get(connectedStationId).get(v).getDateDeparture().getTime();
                        //время ожидания до начала движения
                        // TODO: 29.03.17 пройтись по всем вариантам stationArrivingRoutes
                        long weightWaiting = lines.get(idCurrentStation).get(connectedStationId).get(v).getDateDeparture().getTime() -
//                                arrivedToStation.get(idCurrentStation).getTime();
                                routeTimetableArriving.getDateArrival().getTime();
                        //сумма
                        long fullWeight = weightU + weightWaiting;
//                      duration.get(connectedStationId) - не ясная вещь, так как потом может быть
                        if (duration.get(idCurrentStation) + fullWeight < duration.get(connectedStationId)) { //если путь короче
                            // если дата выезда из А позже даты приезда в неё
                            if (routeTimetableArriving.getDateArrival().before(lines.get(idCurrentStation).get(connectedStationId).get(v).getDateDeparture())) {
                                //добавление
                                ArrayList<RouteTimetables> previousRT = stationArrivingRoutes.get(connectedStationId);
                                if (previousRT.isEmpty()) {
                                    stationArrivingRoutes.put(connectedStationId, new ArrayList<>(Collections.singletonList(lines.get(idCurrentStation).get(connectedStationId).get(v))));
                                } else {
                                    if (!previousRT.contains(lines.get(idCurrentStation).get(connectedStationId).get(v))) {
                                        previousRT.add(lines.get(idCurrentStation).get(connectedStationId).get(v));
                                        stationArrivingRoutes.put(connectedStationId, previousRT);
                                    }
                                }
                                pred.put(connectedStationId, idCurrentStation);
                                duration.put(connectedStationId, duration.get(idCurrentStation) + fullWeight);
                            }
                        }
                    }
                }
            }
            //помечаем вершину idCurrentStation просмотренной, до нее найдено кратчайшее расстояние
            used.put(idCurrentStation, true);
        }
    }
}
