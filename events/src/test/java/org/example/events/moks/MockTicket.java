package org.example.events.moks;

import org.example.events.entity.Event;
import org.example.events.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;


public class MockTicket {
    private PagedResourcesAssembler<Ticket> assembler;

    public MockTicket(PagedResourcesAssembler<Ticket> assembler) {
        this.assembler = assembler;
    }

    public MockTicket() {}

    public Ticket mockTicket(Event mockEvent) {
        Ticket ticket = new Ticket();
        ticket.setTicketId("asdf");
        ticket.setEvent(mockEvent);
        ticket.setCustomerName("John");
        ticket.setCustomerMail("john@example.com");
        ticket.setStatus("CREATED");
        ticket.setBRLamount(10d);
        ticket.setUSDamount(50d);
        return ticket;
    }

    public PagedModel<EntityModel<Ticket>> mockPagedModelTicket() {
        Ticket ticket = mockTicket(new Event());
        EntityModel<Ticket> entityModel = EntityModel.of(ticket);

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(10, 0, 1); // (tamanho da página, página atual, total de itens)
        return PagedModel.of(List.of(entityModel), metadata);
    }

    public PagedModel<EntityModel<Ticket>> mockEmptyPagedModelTicket() {
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(10, 0, 1); // (tamanho da página, página atual, total de itens)
        return PagedModel.of(List.of(), metadata);
    }


    public List<Ticket> mockTickets() {
        Event event = new MockEvent().mockEvent();
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Ticket ticket = new Ticket();
            ticket.setCustomerName("name" +i);
            ticket.setCustomerMail("email"+i);
            ticket.setEvent(event);
            ticket.setBRLamount((double) 10 + i);
            ticket.setUSDamount((double) 10 + i);

            tickets.add(ticket);
        }
        return tickets;
    }

}
