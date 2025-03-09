package org.example.events.unittests.moks;

import org.example.events.entity.Event;
import org.example.events.entity.Ticket;

public class MockTicket {

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
}
