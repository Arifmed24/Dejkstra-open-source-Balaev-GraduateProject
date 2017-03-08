package com.balaev.gradproj.controllers;

import com.balaev.gradproj.domain.Passenger;
import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.service.api.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Controller
public class PassengerController {
    @Autowired
    PassengerService passengerService;

    @RequestMapping(value = "/admin/way/passengers", method = RequestMethod.POST)
    public ModelAndView getPassengersOfWay(HttpServletRequest request) {
        String indexS = request.getParameter("index");
        int i = Integer.parseInt(indexS);
        List<List<RouteTimetables>> ways = (List<List<RouteTimetables>>) request.getSession().getAttribute("ways");
        request.getSession().removeAttribute("ways");
        List<RouteTimetables> way = ways.get(i);
        request.setAttribute("title", "Passengers");
        Set<Passenger> passengers = passengerService.getPassengersOfRoute(way);
        request.setAttribute("passengers", passengers);
        return new ModelAndView("/admin/passenger/autorized-passengers");
    }
}
