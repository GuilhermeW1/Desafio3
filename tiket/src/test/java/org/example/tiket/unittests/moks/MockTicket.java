package org.example.tiket.unittests.moks;

import org.example.tiket.dto.TicketRequestDto;
import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.entity.Event;
import org.example.tiket.entity.Ticket;

import java.math.BigDecimal;

public class MockTicket {
    private static final String TICKET_ID = "uuid";
    private static final String CPF = "04798892017";
    private static final String CUSTOMER_NAME = "John Doe";
    private static final String CUSTOMER_MAIL = "john.doe@example.com";
    private static final Event EVENT = new MockEvent().mockEvent();
    private static final BigDecimal BRL_AMOUNT = new BigDecimal("50");
    private static final BigDecimal USD_AMOUNT = new BigDecimal("10");

    public Ticket mockTicket() {
        Ticket ticket = new Ticket();
        ticket.setTicketId(TICKET_ID);
        ticket.setCpf(CPF);
        ticket.setCustomerName(CUSTOMER_NAME);
        ticket.setCustomerMail(CUSTOMER_MAIL);
        ticket.setEvent(EVENT);
        ticket.setBRLamount(BRL_AMOUNT);
        ticket.setUSDamount(USD_AMOUNT);

        return ticket;
    }

    public TicketRequestDto mockTicketCreateDto() {
        TicketRequestDto ticket = new TicketRequestDto();
        ticket.setTicketId(TICKET_ID);
        ticket.setCustomerName(CUSTOMER_NAME);
        ticket.setCustomerMail(CUSTOMER_MAIL);
        ticket.setEventId(EVENT.getId());
        ticket.setEventName(EVENT.getEventName());
        ticket.setBRLamount(BRL_AMOUNT);
        ticket.setUSDamount(USD_AMOUNT);

        return ticket;
    }
}
