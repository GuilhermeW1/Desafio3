package org.example.tiket.unittests;

import org.example.tiket.dto.TicketRequestDto;
import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.entity.Ticket;
import org.example.tiket.exceptions.CpfNotFoundException;
import org.example.tiket.exceptions.EventNotFoundException;
import org.example.tiket.exceptions.TicketNotFoundException;
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

        assertNotNull(result);
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
        assertEquals(result.getTicketId(), ticket.getTicketId());
        assertEquals(result.getCustomerName(), ticket.getCustomerName());
        assertEquals(result.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(result.getBRLamount(), ticket.getBRLamount());
        assertEquals(result.getUSDamount(), ticket.getUSDamount());
    }

    @Test
    void updateWithInvalidIdThrowsException() {
        String id = "id";

        when(ticketRepository.findByTicketIdAndIsDeletedFalse(anyString()))
                .thenThrow(new TicketNotFoundException("Ticket with id " + id + " not found"));

        TicketRequestDto dto = input.mockTicketCreateDto();

        Exception exception = assertThrows(TicketNotFoundException.class, () -> {
            ticketService.update(dto, id); // Deve lançar exceção
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
        List<Ticket> tickets = Arrays.asList(ticket);

        when(ticketRepository.findByCpfAndIsDeletedFalse("04798892017")).thenReturn(tickets);
        List<TicketResponseDto> result = ticketService.findByCpf("04798892017");

        assertNotNull(result);
        assertEquals(result.get(0).getTicketId(), ticket.getTicketId());
        assertEquals(result.get(0).getCustomerName(), ticket.getCustomerName());
        assertEquals(result.get(0).getCustomerMail(), ticket.getCustomerMail());
        assertEquals(result.get(0).getBRLamount(), 50.00);
        assertEquals(result.get(0).getUSDamount(), 20.00);
    }

    @Test
    void findByCpfWithInvalidCpf() {
        String cpf = "9090";
        when(ticketRepository.findByCpfAndIsDeletedFalse(cpf)).thenThrow(new CpfNotFoundException("Ticket with id " + cpf + " not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.findByCpf(cpf); // Método que deve lançar a exceção
        });

        assertEquals("Ticket with id " + cpf +" not found", exception.getMessage());
    }

    @Test
    void findTiketsByEventId() {
        List<Ticket> tickets = input.mockTicketList();

        when(ticketRepository.findAllByEvent_IdAndIsDeletedFalse(anyString())).thenReturn(tickets);

        List<TicketResponseDto> result = ticketService.findAllByEventId("any");

        for (int i = 0; i < tickets.size(); i++) {
            assertNotNull(result);
            assertEquals(result.get(i).getTicketId(), tickets.get(i).getTicketId());
            assertEquals(result.get(i).getCustomerName(), tickets.get(i).getCustomerName());
            assertEquals(result.get(i).getCustomerMail(), tickets.get(i).getCustomerMail());
            assertEquals(result.get(i).getBRLamount(), 50.00 + i);
            assertEquals(result.get(i).getUSDamount(), 20.00 + i);
        }


    }

}
