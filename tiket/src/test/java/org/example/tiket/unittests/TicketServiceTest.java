package org.example.tiket.unittests;

import org.example.tiket.dto.TicketRequestDto;
import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.entity.Ticket;
import org.example.tiket.exceptions.EventNotFoundException;
import org.example.tiket.exceptions.TicketNotFoundException;
import org.example.tiket.repository.TicketRepository;
import org.example.tiket.service.EventService;
import org.example.tiket.service.TicketService;
import org.example.tiket.moks.MockEvent;
import org.example.tiket.moks.MockTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
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

        when(ticketRepository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));

        TicketResponseDto result = ticketService.findById("1");

        System.out.println(result.toString());
        assertEquals("links: [</api/tickets/v1/get-ticket/uuid>;rel=\"self\"]", result.toString());
        assertEquals(result.getTicketId(), ticket.getTicketId());
        assertEquals(result.getCustomerName(), ticket.getCustomerName());
        assertEquals(result.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(result.getBRLamount(), ticket.getBRLamount());
        assertEquals(result.getUSDamount(), ticket.getUSDamount());
    }

    @Test
    void findByIdNotFoundThrowsException() {
        String id = "uuid";
        when(ticketRepository.findByTicketIdAndIsDeletedFalse(anyString())).thenThrow(new TicketNotFoundException("Ticket with id " + id + " not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.findById(id); // Método que deve lançar a exceção
        });

        assertEquals("Ticket with id " + id +" not found", exception.getMessage());

    }

    @Test
    void create() {
        Ticket ticket = input.mockTicket();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(eventService.getEvent(anyString())).thenReturn(new MockEvent().mockEvent());

        TicketRequestDto dto = input.mockTicketCreateDto();

        TicketResponseDto result = ticketService.create(dto);

        assertNotNull(result);
        assertEquals("links: [</api/tickets/v1/get-ticket/uuid>;rel=\"self\"]", result.toString());
        assertEquals(result.getTicketId(), ticket.getTicketId());
        assertEquals(result.getCustomerName(), ticket.getCustomerName());
        assertEquals(result.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(result.getBRLamount(), 50.00);
        assertEquals(result.getUSDamount(), 20.00);
        assertEquals(result.getStatus(), "CREATED");
    }

    @Test
    void createTicketWithInvalidEventIdThrowsException() {
        Ticket ticket = input.mockTicket();

        when(eventService.getEvent(anyString())).thenThrow(new EventNotFoundException("Event with id " + ticket.getEvent().getId() + " not found"));
        TicketRequestDto dto = input.mockTicketCreateDto();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.create(dto);
        });

        assertEquals("Event with id " + ticket.getEvent().getId() + " not found", exception.getMessage());
    }

    @Test
    void update() {
        Ticket ticket = input.mockTicket();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketRepository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));
        when(eventService.getEvent(anyString())).thenReturn(new MockEvent().mockEvent());

        TicketRequestDto dto = input.mockTicketCreateDto();

        TicketResponseDto result = ticketService.update(dto, ticket.getTicketId());

        assertNotNull(result);
        assertEquals("links: [</api/tickets/v1/get-ticket/uuid>;rel=\"self\"]", result.toString());
        assertEquals(result.getTicketId(), ticket.getTicketId());
        assertEquals(result.getCustomerName(), ticket.getCustomerName());
        assertEquals(result.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(result.getBRLamount(), ticket.getBRLamount());
        assertEquals(result.getUSDamount(), ticket.getUSDamount());
        assertNotNull(result.getStatus());
        assertNotNull(ticket.getDeleted());
        assertNull(ticket.getDeletedAt());
        assertNotNull(ticket.getUpdatedAt());
        assertNotNull(ticket.getCreatedAt());
    }

    @Test
    void update_WithInvalidTicektId_ThrowsException() {
        String id = "id";

        when(ticketRepository.findByTicketIdAndIsDeletedFalse(anyString()))
                .thenThrow(new TicketNotFoundException("Ticket with id " + id + " not found"));

        TicketRequestDto dto = input.mockTicketCreateDto();

        Exception exception = assertThrows(TicketNotFoundException.class, () -> {
            ticketService.update(dto, id);
        });

        assertEquals("Ticket with id " + id + " not found", exception.getMessage());
    }

    @Test
    void updateWithInvalidEventIdThrowsException() {
        Ticket ticket = input.mockTicket();

        when(ticketRepository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));
        when(eventService.getEvent(anyString())).thenThrow(new EventNotFoundException("Event with id " + ticket.getEvent().getId() + " not found"));
        TicketRequestDto dto = input.mockTicketCreateDto();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.update(dto, ticket.getTicketId()); // Deve lançar exceção
        });

        assertEquals("Event with id " + ticket.getEvent().getId() + " not found", exception.getMessage());
    }

    @Test
    void delete() {
        Ticket ticket = input.mockTicket();
        when(ticketRepository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));
        ticketService.delete(ticket.getTicketId());
    }

    @Test
    void findByCpf() {
        Ticket ticket = input.mockTicket();
        Ticket ticket2 = input.mockTicket();
        List<Ticket> tickets = Arrays.asList(ticket, ticket2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(tickets, pageable, tickets.size());

        when(ticketRepository.findByCpfAndIsDeletedFalse("04798892017", pageable)).thenReturn(page);
        Page<TicketResponseDto> result = ticketService.findByCpf("04798892017", pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        for (int i = 0; i < tickets.size(); i++) {
            assertNotNull(result.getContent().get(i).toString());
            assertEquals(result.getContent().get(i).getTicketId(), tickets.get(i).getTicketId());
            assertEquals(result.getContent().get(i).getCustomerName(), tickets.get(i).getCustomerName());
            assertEquals(result.getContent().get(i).getCustomerMail(), tickets.get(i).getCustomerMail());
            assertEquals(result.getContent().get(i).getBRLamount(), tickets.get(i).getBRLamount());
            assertEquals(result.getContent().get(i).getUSDamount(), tickets.get(i).getUSDamount());
        }
    }

    @Test
    void findTiketsByEventId() {
        List<Ticket> tickets = input.mockTicketList();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(tickets, pageable, tickets.size());
        when(ticketRepository.findAllByEvent_IdAndIsDeletedFalse(anyString(), any(Pageable.class))).thenReturn(page);

        Page<TicketResponseDto> result = ticketService.findAllByEventId("any", pageable);
        assertNotNull(result);
        assertEquals(tickets.size(), result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(tickets.size(), result.getContent().size());

        for (int i = 0; i < tickets.size(); i++) {
            assertEquals("links: [</api/tickets/v1/get-ticket/uuid"+i+">;rel=\"self\"]", result.getContent().get(i).toString());
            assertEquals(result.getContent().get(i).getTicketId(), tickets.get(i).getTicketId());
            assertEquals(result.getContent().get(i).getCustomerName(), tickets.get(i).getCustomerName());
            assertEquals(result.getContent().get(i).getCustomerMail(), tickets.get(i).getCustomerMail());
            assertEquals(result.getContent().get(i).getBRLamount(), 50.00 + i);
            assertEquals(result.getContent().get(i).getUSDamount(), 20.00 + i);
        }


    }

}
