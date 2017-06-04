package com.balaev.gradproj.controllers;

import com.balaev.gradproj.domain.Passenger;
import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Ticket;
import com.balaev.gradproj.domain.User;
import com.balaev.gradproj.service.api.PassengerService;
import com.balaev.gradproj.service.api.RouteTimetablesService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class TicketController {

    @Autowired
    PassengerService passengerService;
    @Autowired
    RouteTimetablesService routeTimetablesService;
    @Autowired
    TicketService ticketService;

    @RequestMapping(value = "/admin/ticket/add", method = RequestMethod.POST)
    public ModelAndView newTicketForm(HttpServletRequest request) {
        String first = request.getParameter("first");
        String last = request.getParameter("last");
        String b = request.getParameter("birth");
        //validation
        if (ValidationUtils.checkName(first)) {
            if (ValidationUtils.checkName(last)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date birth = null;
                try {
                    birth = sdf.parse(b);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Passenger passenger = new Passenger();
                passenger.setBirth(birth);
                passenger.setFirstName(first);
                passenger.setLastName(last);
                Passenger ticketPassenger;
                //check passenger. if not exists - creates new one
                if (passengerService.isExists(passenger)) {
                    ticketPassenger = passengerService.getByNameAndBirth(passenger);
                } else {
                    ticketPassenger = passengerService.create(passenger);
                }

                Ticket t = (Ticket) request.getSession().getAttribute("ticket");
                List<RouteTimetables> way = (List<RouteTimetables>) request.getSession().getAttribute("way");
                Set<RouteTimetables> ticketWay = new HashSet<>(way);

                //check registration of this passenger in variant
                Set<Passenger> passengers = passengerService.getPassengersOfRoute(way);
                //flag
                boolean hasPassenger = false;
                //if this passenger exists in variant
                for (Passenger routePassenger : passengers) {
                    if (ticketPassenger.equals(routePassenger)) {
                        hasPassenger = true;
                    }
                }
                if (hasPassenger == false) {
                    //final ticket
                    Ticket ticket = new Ticket();
                    ticket.setTicketPassenger(ticketPassenger);
                    ticket.setDepartureStation(t.getDepartureStation());
                    ticket.setDepartureDate(t.getDepartureDate());
                    ticket.setArrivalStation(t.getArrivalStation());
                    ticket.setArrivalDate(t.getArrivalDate());
                    ticket.setPrice(t.getPrice());
                    ticket.setTicketTrain(t.getTicketTrain());
                    ticket.setRouteTimetables(ticketWay);
                    User user1 = (User) request.getSession().getAttribute("user");
                    ((User) request.getSession().getAttribute("user")).addTicket(ticket);
                    ticket = ticketService.createTicket(ticket);

                    for (RouteTimetables rt : way) {
                        rt.getTickets().add(ticket);
                        rt.setFreeSeats(rt.getFreeSeats() - 1);
                        routeTimetablesService.updateRouteTimetable(rt);
                    }
                    request.getSession().removeAttribute("way");
                    request.getSession().removeAttribute("ticket");
                    request.setAttribute("ticket", ticket);
                    request.setAttribute("title", "Ticket");
                    return new ModelAndView("/ticket/view-ticket");
                } else {
                    request.setAttribute("error", "This passenger is registered yet");
                    request.setAttribute("way", way);
                    request.setAttribute("ticket", t);
                    request.setAttribute("title", "Passenger");
                    return newTicket(request);
                }
            } else {
                List<RouteTimetables> way = (List<RouteTimetables>) request.getSession().getAttribute("way");
                request.setAttribute("errorL", "Wrong last name");
                request.setAttribute("way", way);
                request.setAttribute("title", "Passenger");
                return newTicket(request);
            }
        } else {
            List<RouteTimetables> way = (List<RouteTimetables>) request.getSession().getAttribute("way");
            request.setAttribute("errorF", "Wrong first name");
            request.setAttribute("way", way);
            request.setAttribute("title", "Passenger");
            return newTicket(request);
        }
    }

    @RequestMapping(value = "/admin/ticket/add", method = RequestMethod.GET)
    public ModelAndView newTicket(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String indexS = request.getParameter("index");
        int i = Integer.parseInt(indexS);
        List<List<RouteTimetables>> ways = (List<List<RouteTimetables>>) request.getSession().getAttribute("ways");
        List<Ticket> tickets = (List<Ticket>) request.getSession().getAttribute("tickets");
        List<RouteTimetables> way = ways.get(i);
        Ticket ticket = tickets.get(i);
        request.getSession().removeAttribute("ways");
        request.getSession().removeAttribute("tickets");
        request.getSession().setAttribute("way", way);
        request.getSession().setAttribute("ticket", ticket);
        modelAndView.addObject("title", "Passenger");
        modelAndView.setViewName("/passenger/new-passenger");
        return modelAndView;
    }

    @RequestMapping(value = "/user/tickets", method = RequestMethod.GET)
    public ModelAndView userTickets() {
        ModelAndView modelAndView = new ModelAndView("/ticket/user-tickets");
        modelAndView.addObject("title", "User Tickets");
        return modelAndView;
    }
}
