package com.balaev.gradproj.controllers;

import com.balaev.gradproj.domain.*;
import com.balaev.gradproj.service.api.*;
import com.balaev.gradproj.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Controller
public class RouteController {
    @Autowired
    RouteTimetablesService routeTimetablesService;
    @Autowired
    RouteService routeService;
    @Autowired
    StationService stationService;
    @Autowired
    TrainService trainService;
    @Autowired
    TimetableService timetableService;

    @RequestMapping(value = "/routes", method = RequestMethod.GET)
    public ModelAndView getAllRoutes() {
        ModelAndView modelAndView = new ModelAndView("/route/all-routes");
        List<Route> routes = routeService.getAllRoutes();
        modelAndView.addObject("routes", routes);
        modelAndView.addObject("title", "Routes");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/graphic/add", method = RequestMethod.GET)
    public ModelAndView getNewGraphicForm(HttpServletRequest request) {
        Integer idRoute = Integer.parseInt(request.getParameter("idRoute"));
        Route route = routeService.readRoute(idRoute);
        List<RouteTimetables> routeTimetables = routeTimetablesService.createTemplateOfGraphic(route);
        request.getSession().setAttribute("routeTimetables", routeTimetables);
        request.getSession().setAttribute("route", route);
        request.setAttribute("title", "Add graphic");
        return new ModelAndView("/admin/route/add-graphic");
    }

    @RequestMapping(value = "/admin/graphic/add", method = RequestMethod.POST)
    public ModelAndView getNewGraphic(HttpServletRequest request) {
        Enumeration enumeration = request.getParameterNames();
        List<Date> dates = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            Object obj = enumeration.nextElement();
            String param = (String) obj;
            String value = request.getParameter(param);

            if (ValidationUtils.checkDatetime(value)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dates.add(date);
            } else {
                request.setAttribute("error", "Incorrect date");
                request.setAttribute("title", "Add graphic");
                return getNewGraphicForm(request);
            }
        }
        Route route = (Route) request.getSession().getAttribute("route");
        List<RouteTimetables> routeTimetables;
        try {
            routeTimetables = routeTimetablesService.addDateInGraphic(dates, route);
            routeTimetables = routeTimetablesService.createGraphic(routeTimetables);
            request.getSession().removeAttribute("routeTimetables");
            request.getSession().removeAttribute("route");
            return new ModelAndView("redirect:/routes");
        } catch (Exception e) {
            e.getMessage();
            request.setAttribute("error", e.getMessage());
            request.setAttribute("title", "Add graphic");
            return getNewGraphicForm(request);
        }
    }

    @RequestMapping(value = "/admin/route/add", method = RequestMethod.GET)
    public ModelAndView getNewRouteForm(HttpServletRequest request) {
        List<Station> stations = stationService.getAllStations();
        List<Train> trains = trainService.getAllTrains();
        request.getSession().setAttribute("stations", stations);
        request.getSession().setAttribute("trains", trains);
        request.setAttribute("title", "New route");
        return new ModelAndView("/admin/route/add-route");
    }

    @RequestMapping(value = "/admin/route/add", method = RequestMethod.POST)
    public ModelAndView getNewRoute(HttpServletRequest request) {
        String routeName = request.getParameter("routeName");
        String idTrain = request.getParameter("train");
        String startStationId = request.getParameter("startStation");
        String finishStationId = request.getParameter("finishStation");

        if (startStationId.equals(finishStationId)) {
            request.setAttribute("error", "Start station and finish station must be different");
            request.setAttribute("title", "New route");
            return getNewRouteForm(request);
        } else {
            if (ValidationUtils.checkRouteName(routeName)) {

                Integer intTrainId = Integer.parseInt(idTrain);
                Integer intStartStationId = Integer.parseInt(startStationId);
                Integer intFinishStationId = Integer.parseInt(finishStationId);

                Train train = trainService.read(intTrainId);
                Station startStation = stationService.read(intStartStationId);
                Station finishStation = stationService.read(intFinishStationId);

                Route route = new Route();
                route.setRouteName(routeName);
                route.setTrain(train);
                route.setStartStation(startStation);
                route.setFinishStation(finishStation);
                route = routeService.createRoute(route);

                request.getSession().setAttribute("route", route);

                request.getSession().removeAttribute("stations");
                request.getSession().removeAttribute("trains");

                return new ModelAndView("redirect:/admin/route/elements");
            } else {
                request.setAttribute("error", "Incorrect route name");
                request.setAttribute("title", "New route");
                return getNewRouteForm(request);
            }
        }
    }

    @RequestMapping(value = "/admin/route/elements", method = RequestMethod.GET)
    public ModelAndView getNumberOfElementsForm() {
        ModelAndView modelAndView = new ModelAndView("/admin/route/number-of-elements");
        modelAndView.addObject("title", "Elements in route");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/route/elements", method = RequestMethod.POST)
    public ModelAndView getNumberOfElements(HttpServletRequest request) {
        String elements = request.getParameter("elements");
        if (ValidationUtils.checkNumberOfStationsInRoute(elements)) {
            int stationsInRoute = Integer.parseInt(elements);
            request.getSession().setAttribute("stationsInRoute", stationsInRoute);
            return new ModelAndView("redirect:/admin/route/elements/fill");
        } else {
            request.setAttribute("error", "Please write number from 2 to 5");
            request.setAttribute("title", "Elements in route");
            return getNumberOfElementsForm();
        }
    }

    @RequestMapping(value = "/admin/route/elements/fill", method = RequestMethod.GET)
    public ModelAndView fillNewRouteForm(HttpServletRequest request) {
        int numberOfStations = (int) request.getSession().getAttribute("stationsInRoute");
        Route route = (Route) request.getSession().getAttribute("route");
        List<Station> stations = stationService.getAllStations();
        request.getSession().setAttribute("stations", stations);
        List<RouteTimetables> graphicOfRoute = new ArrayList<>();
        for (int i = 0; i < numberOfStations - 1; i++) {
            RouteTimetables r = new RouteTimetables();
            graphicOfRoute.add(r);
        }
        request.setAttribute("title", "Fill new route");
        request.setAttribute("route", route);
        request.getSession().setAttribute("number", numberOfStations - 1);
        request.getSession().setAttribute("elements", graphicOfRoute);
        return new ModelAndView("/admin/route/fill-route");
    }

    @RequestMapping(value = "/admin/route/elements/fill", method = RequestMethod.POST)
    public ModelAndView fillNewRoute(HttpServletRequest request) {
        Enumeration enumeration = request.getParameterNames();
        List<String> values = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            Object obj = enumeration.nextElement();
            String param = (String) obj;
            String value = request.getParameter(param);
            values.add(value);
        }
        List<String> stations = new ArrayList<>();
        for (int i = 0; i < values.size() - 2; i = i + 3) {
            stations.add(values.get(i));
        }
        for (int i = values.size() - 3; i >= 0; i--) {
            if (i % 3 == 0) {
                values.remove(i);
            }
        }
        Route route = (Route) request.getSession().getAttribute("route");
        request.getSession().removeAttribute("stationsInRoute");

        try {
            List<Station> routeStations = stationService.checkStationsInRoute(route, stations);
            List<Timetable> routeTimetables = new ArrayList<>();
            for (int i = 0; i < routeStations.size() - 1; i++) {
                Timetable newTimetable = timetableService.readByStations(routeStations.get(i), routeStations.get(i + 1));
                routeTimetables.add(newTimetable);
            }
            List<Date> routeDates = routeTimetablesService.checkDatesInRoute(values);
            for (int i = 0; i < routeTimetables.size(); i++) {
                RouteTimetables newRoutetimetable = new RouteTimetables();
                newRoutetimetable.setNumberInRoute(i + 1);
                newRoutetimetable.setLine(routeTimetables.get(i));
                newRoutetimetable.setRouteId(route);
                newRoutetimetable.setFreeSeats(route.getTrain().getSeats());
                newRoutetimetable.setDateDeparture(routeDates.get(i * 2));
                newRoutetimetable.setDateArrival(routeDates.get(i * 2 + 1));
                newRoutetimetable = routeTimetablesService.createRoutetimetable(newRoutetimetable);
            }
            request.removeAttribute("number");
            request.removeAttribute("route");
            request.removeAttribute("elements");
            request.removeAttribute("stations");
            return new ModelAndView("redirect:/routes");
        } catch (Exception e) {
            e.getMessage();
            request.setAttribute("error", e.getMessage());
            request.setAttribute("title", "Fill new route");
            return fillNewRouteForm(request);
        }
    }
}
