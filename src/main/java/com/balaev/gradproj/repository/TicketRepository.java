package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
}
