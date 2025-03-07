package org.example.tiket.unittests;

import org.example.tiket.dto.TicketRequestDto;
import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.entity.Ticket;
import org.example.tiket.repository.TicketRepository;
import org.example.tiket.service.EventService;
import org.example.tiket.service.TicketService;
import org.example.tiket.unittests.moks.MockEvent;
import org.example.tiket.unittests.moks.MockTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    MockTicket input;

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setUp() {
        input = new MockTicket();
    }

    @Test
    void findById() {
        Ticket ticket = input.mockTicket();

        when(ticketRepository.findByAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));

        TicketResponseDto result = ticketService.findById("1");

        assertNotNull(result);
        assertEquals(result.getTicketId(), ticket.getTicketId());
        assertEquals(result.getCustomerName(), ticket.getCustomerName());
        assertEquals(result.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(result.getBRLamount(), ticket.getBRLamount());
        assertEquals(result.getUSDamount(), ticket.getUSDamount());
    }

    @Test
    void create() {
        Ticket ticket = input.mockTicket();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(eventService.getEvent(anyString())).thenReturn(new MockEvent().mockEvent());

        TicketRequestDto dto = input.mockTicketCreateDto();

        TicketResponseDto result = ticketService.create(dto);

        assertNotNull(result);
        assertEquals(result.getTicketId(), ticket.getTicketId());
        assertEquals(result.getCustomerName(), ticket.getCustomerName());
        assertEquals(result.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(result.getBRLamount(), ticket.getBRLamount());
        assertEquals(result.getUSDamount(), ticket.getUSDamount());
    }

    @Test
    void update() {
        Ticket ticket = input.mockTicket();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(eventService.getEvent(anyString())).thenReturn(new MockEvent().mockEvent());
        when(ticketRepository.findByAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));

        TicketRequestDto dto = input.mockTicketCreateDto();

        TicketResponseDto result = ticketService.update(dto, ticket.getTicketId());

        assertNotNull(result);
        assertEquals(result.getTicketId(), ticket.getTicketId());
        assertEquals(result.getCustomerName(), ticket.getCustomerName());
        assertEquals(result.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(result.getBRLamount(), ticket.getBRLamount());
        assertEquals(result.getUSDamount(), ticket.getUSDamount());
    }

    void delete() {
        Ticket ticket = input.mockTicket();
        when(ticketRepository.findByAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));
        ticketService.delete(ticket.getTicketId());
    }

}
