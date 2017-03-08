package com.balaev.gradproj.controllers;

import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.service.api.RouteTimetablesService;
import com.balaev.gradproj.service.api.StationService;
import com.balaev.gradproj.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class StationController {
    @Autowired
    StationService stationService;
    @Autowired
    RouteTimetablesService routeTimetablesService;

    @RequestMapping(value = "/stations", method = RequestMethod.GET)
    public ModelAndView stations() {
        ModelAndView modelAndView = new ModelAndView();
        List<Station> allStations = stationService.getAllStations();
        modelAndView.addObject("stations", allStations);
        modelAndView.addObject("title", "Stations");
        modelAndView.setViewName("/station/all-stations");
        return modelAndView;
    }

    @RequestMapping(value = "/station/timetable", method = RequestMethod.GET)
    public ModelAndView stationTimetable(HttpServletRequest request) {
        List<Station> allStations = stationService.getAllStations();
        ModelAndView modelAndView = new ModelAndView("/station/timetable-date");
        String idStation = request.getParameter("idStation");
        String nameStation = request.getParameter("nameStation");
        request.getSession().setAttribute("nameStation", request.getParameter("nameStation"));
        request.getSession().setAttribute("idStation", request.getParameter("idStation"));
        request.getSession().setAttribute("stations", allStations);
        modelAndView.addObject("idStation", idStation);
        modelAndView.addObject("nameStation", nameStation);
        modelAndView.addObject("stations", allStations);
        modelAndView.addObject("title", "Timetable date");
        return modelAndView;
    }

    @RequestMapping(value = "/station/timetable", method = RequestMethod.POST)
    public ModelAndView showTimetableOfStation(HttpServletRequest request) throws ServletException, IOException {
        String dateString = request.getParameter("date");
        String stationId = request.getParameter("stationId");
        ModelAndView modelAndView = new ModelAndView();

        if (ValidationUtils.checkDate(dateString)) {
            int id = Integer.parseInt(stationId);
            Station station = stationService.read(id);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<RouteTimetables> routeTimetablesArr = routeTimetablesService.getTimetableStationArr(station, date);
            List<RouteTimetables> routeTimetablesDep = routeTimetablesService.getTimetableStationDep(station, date);
            modelAndView.addObject("station", station.getStationName());
            modelAndView.addObject("date", date);
            modelAndView.addObject("tablesArr", routeTimetablesArr);
            modelAndView.addObject("tablesDep", routeTimetablesDep);
            modelAndView.addObject("title", "Station timetable");
            request.getSession().removeAttribute("nameStation");
            request.getSession().removeAttribute("idStation");
            request.getSession().removeAttribute("stations");
            modelAndView.setViewName("/station/station-timetable");
            return modelAndView;
        } else {
            modelAndView.addObject("title", "Station");
            modelAndView.addObject("error", "Incorrect date");
            modelAndView.setViewName("/station/timetable-date");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/admin/station/add", method = RequestMethod.GET)
    public ModelAndView addStationForm() {
        ModelAndView modelAndView = new ModelAndView("/admin/station/add-station");
        modelAndView.addObject("title", "Create station");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/station/add", method = RequestMethod.POST)
    public ModelAndView addStation(HttpServletRequest request) {
        String name = request.getParameter("name");
        if (ValidationUtils.checkStationName(name) && name.length() < 50) {
            Station station = new Station();
            station.setStationName(name);
            stationService.createStation(station);
            return stations();
        } else {
            request.setAttribute("error", "Wrong name");
            return addStationForm();
        }
    }

    @RequestMapping(value = "/admin/station/update", method = RequestMethod.GET)
    public ModelAndView updateStationForm(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/admin/station/update-station");
        Integer idStation = Integer.parseInt(request.getParameter("idStation"));
        Station station = stationService.read(idStation);
        request.setAttribute("station", station);
        modelAndView.addObject("title", "Update station");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/station/update", method = RequestMethod.POST)
    public ModelAndView updateStation(HttpServletRequest request) {
        String name = request.getParameter("stationName");
        String id = request.getParameter("idStation");

        if (ValidationUtils.checkStationName(name) && name.length() < 50) {
            Station station = stationService.read(Integer.parseInt(id));
            station.setStationName(name);
            stationService.updateStation(station);
            return stations();
        } else {
            request.setAttribute("error", "Wrong name");
            return updateStationForm(request);
        }
    }
}
