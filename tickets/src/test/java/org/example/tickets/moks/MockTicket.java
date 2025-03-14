package org.example.tickets.moks;

import org.example.tickets.dto.TicketRequestDto;
import org.example.tickets.entity.Event;
import org.example.tickets.entity.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockTicket {
    private static final String TICKET_ID = "uuid";
    private static final String CPF = "04798892017";
    private static final String CUSTOMER_NAME = "John Doe";
    private static final String CUSTOMER_MAIL = "john.doe@example.com";
    private static final Event EVENT = new MockEvent().mockEvent();
    private static final Double BRL_AMOUNT = 50.00;
    private static final Double USD_AMOUNT = 20.00;

    public Ticket mockTicket() {
        Ticket ticket = new Ticket();
        ticket.setTicketId(TICKET_ID);
        ticket.setCpf(CPF);
        ticket.setCustomerName(CUSTOMER_NAME);
        ticket.setCustomerMail(CUSTOMER_MAIL);
        ticket.setEvent(EVENT);
        ticket.setBRLamount(BRL_AMOUNT);
        ticket.setUSDamount(USD_AMOUNT);
        ticket.setStatus("COMPLETED");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(null);
        ticket.setDeleted(false);
        ticket.setDeletedAt(null);

        return ticket;
    }

    public TicketRequestDto mockTicketCreateDto() {
        TicketRequestDto ticket = new TicketRequestDto();
        ticket.setCpf(CPF);
        ticket.setCustomerName(CUSTOMER_NAME);
        ticket.setCustomerMail(CUSTOMER_MAIL);
        ticket.setEventId(EVENT.getId());
        ticket.setEventName(EVENT.getEventName());
        ticket.setBrlAmount(BRL_AMOUNT);
        ticket.setUsdAmount(USD_AMOUNT);

        return ticket;
    }

    public List<Ticket> mockTicketList() {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Ticket ticket = new Ticket();
            ticket.setTicketId(TICKET_ID + i);
            ticket.setCpf(CPF);
            ticket.setCustomerName(CUSTOMER_NAME + i);
            ticket.setCustomerMail(CUSTOMER_MAIL + i);
            ticket.setEvent(EVENT);
            ticket.setBRLamount(BRL_AMOUNT + i);
            ticket.setUSDamount(USD_AMOUNT + i);
            ticket.setStatus("COMPLETED");

            tickets.add(ticket);
        }
        return tickets;
    }
}
