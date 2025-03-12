package org.example.events.service;

import org.example.events.entity.Ticket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "Ticket", url = "http://localhost:8081/api/tickets/v1")
public interface TicketService {

    @GetMapping("/check-tickets-by-event/{eventId}")
    PagedModel<EntityModel<Ticket>> checkTicketsByEventId(@PathVariable String eventId);
}
