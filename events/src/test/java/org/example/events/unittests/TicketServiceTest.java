package org.example.events.unittests;

import org.example.events.entity.Event;
import org.example.events.entity.Ticket;
import org.example.events.service.TicketService;
import org.example.events.moks.MockEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    MockEvent input;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    void init() {
        this.input = new MockEvent();
    }


    public List<Ticket> mockTickets() {
        Event event = input.mockEvent();
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

    @Test
    void getTicketsByEventId() {
        List<Ticket> tickets = mockTickets();
        Page<Ticket> page = new PageImpl<>(tickets, PageRequest.of(0, 10), tickets.size());
        when(ticketService.checkTickets(anyString())).thenReturn(page);
        var result = ticketService.checkTickets("teste");

        assertNotNull(result);
        assertEquals(3, tickets.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(tickets.get(i).getTicketId(), result.getContent().get(i).getTicketId());
            assertEquals(tickets.get(i).getCustomerName(), result.getContent().get(i).getCustomerName());
            assertEquals(tickets.get(i).getCustomerMail(), result.getContent().get(i).getCustomerMail());
            assertEquals(tickets.get(i).getEvent().getId(), result.getContent().get(i).getEvent().getId());
            assertEquals(tickets.get(i).getBRLamount(), result.getContent().get(i).getBRLamount());
            assertEquals(tickets.get(i).getUSDamount(), result.getContent().get(i).getUSDamount());
        }
    }

}
