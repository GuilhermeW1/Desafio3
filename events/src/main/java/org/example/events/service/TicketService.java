package org.example.events.service;

import org.example.events.entity.Ticket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "Ticket", url = "http://localhost:8081/api/tickets/v1")
public interface TicketService {

    @GetMapping("/check-tickets-by-event/{eventId}")
    List<Ticket> checkTickets(@PathVariable String eventId);
}
