package com.balaev.gradproj.controllers;

import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.domain.Ticket;
import com.balaev.gradproj.service.api.RouteTimetablesService;
import com.balaev.gradproj.service.api.StationService;
import com.balaev.gradproj.service.api.TicketService;
import com.balaev.gradproj.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class RouteTimetablesController {
    @Autowired
    StationService stationService;
    @Autowired
    RouteTimetablesService routeTimetablesService;
    @Autowired
    TicketService ticketService;

    @RequestMapping(value = "/findway", method = RequestMethod.GET)
    public ModelAndView findWayForm() {
        List<Station> stations = stationService.getAllStations();
        ModelAndView modelAndView = new ModelAndView("/route/find-way");
        modelAndView.addObject("stations", stations);
        modelAndView.addObject("title", "Find way");
        return modelAndView;
    }

    @RequestMapping(value = "/findway", method = RequestMethod.POST)
    public ModelAndView findWay(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String DepId = request.getParameter("stationDep");
        String ArrId = request.getParameter("stationArr");
        String dDep = request.getParameter("dateDep");
        String dArr = request.getParameter("dateArr");
        String search = request.getParameter("search");

        int stationDepId = Integer.parseInt(DepId);
        Station stationDep = stationService.read(stationDepId);

        int stationArrId = Integer.parseInt(ArrId);
        Station stationArr = stationService.read(stationArrId);
        if (stationArrId == stationDepId) {
            request.setAttribute("errorStation", "Stations must be different");
            return findWayForm();
        }
        if (ValidationUtils.checkDate(dDep)) {
            if (ValidationUtils.checkDate(dArr)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateDep = null;
                Date dateArr = null;
                try {
                    dateDep = sdf.parse(dDep);
                    dateArr = sdf.parse(dArr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (search.equals("ways")) {
                    List<List<RouteTimetables>> ways = routeTimetablesService.getShortestWay(stationDep, stationArr, dateDep, dateArr);
                    List<Ticket> tickets = ticketService.getTicketsFromRtLists(ways);
                    request.getSession().setAttribute("ways", ways);
                    request.getSession().setAttribute("tickets", tickets);
                    modelAndView.addObject("title", "Ways");
                    if (ways.size() == 0) {
                        modelAndView.addObject("error", "No variants for your request");
                    }
                    modelAndView.setViewName("/route/ways");
                } else {
                    List<List<RouteTimetables>> ways = routeTimetablesService.findWay2(stationDep, stationArr, dateDep, dateArr);
                    List<Ticket> tickets = ticketService.getTicketsFromRtLists(ways);
                    request.getSession().setAttribute("ways", ways);
                    request.getSession().setAttribute("tickets", tickets);
                    modelAndView.addObject("title", "Ways");
                    if (ways.size() == 0) {
                        modelAndView.addObject("error", "No variants for your request");
                    }
                    modelAndView.setViewName("/admin/passenger/ways-with-passengers");
                }
                return modelAndView;
            } else {
                request.setAttribute("errorA", "Incorrect date");
                return findWayForm();
            }
        } else {
            request.setAttribute("errorD", "Incorrect date");
            return findWayForm();
        }
    }
}
