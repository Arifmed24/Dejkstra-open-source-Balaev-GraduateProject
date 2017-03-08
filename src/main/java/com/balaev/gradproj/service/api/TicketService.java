package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.RouteTimetables;
import com.balaev.gradproj.domain.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getTicketsFromRtLists(List<List<RouteTimetables>> ways);

    Ticket createTicket(Ticket ticket);
}
