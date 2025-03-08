package org.example.events.unittests;

import org.example.events.entity.Event;
import org.example.events.entity.Ticket;
import org.example.events.service.TicketService;
import org.example.events.unittests.moks.MockEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        when(ticketService.checkTickets(anyString())).thenReturn(tickets);
        var result = ticketService.checkTickets("teste");

        assertNotNull(result);
        assertEquals(3, tickets.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(tickets.get(i).getTicketId(), result.get(i).getTicketId());
            assertEquals(tickets.get(i).getCustomerName(), result.get(i).getCustomerName());
            assertEquals(tickets.get(i).getCustomerMail(), result.get(i).getCustomerMail());
            assertEquals(tickets.get(i).getEvent().getId(), result.get(i).getEvent().getId());
            assertEquals(tickets.get(i).getBRLamount(), result.get(i).getBRLamount());
            assertEquals(tickets.get(i).getUSDamount(), result.get(i).getUSDamount());
        }
    }

}
