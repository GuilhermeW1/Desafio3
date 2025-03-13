package org.example.events.unittests;

import feign.FeignException;
import org.example.events.dto.EventRequestDto;
import org.example.events.dto.EventResponseDto;
import org.example.events.entity.Event;
import org.example.events.entity.ViaCep;
import org.example.events.exceptions.CepNotFoundException;
import org.example.events.exceptions.EventNotFoundException;
import org.example.events.exceptions.TicketsAssociatedWithEventException;
import org.example.events.repository.EventRepository;
import org.example.events.service.EventService;
import org.example.events.service.TicketService;
import org.example.events.service.ViaCepService;
import org.example.events.moks.MockEvent;
import org.example.events.moks.MockTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    MockEvent event;
    MockTicket ticket;

    @Mock
    TicketService ticketService;

    @InjectMocks
    private EventService service;

    @Mock
    private ViaCepService viaCepService;

    @Mock
    private EventRepository repository;

    @BeforeEach
    void setUp() {
        event = new MockEvent();
        ticket = new MockTicket();
    }

    @Test
    public void createEvent() {
        Event entity = event.mockEvent();

        when(repository.save(any(Event.class))).thenReturn(entity);
        when(viaCepService.getCepInfo(anyString())).thenReturn(new ViaCep());

        EventRequestDto dto = event.mockEventCreateDto();

        EventResponseDto result = service.create(dto);

        assertNotNull(result);
        assertEquals("links: [</api/events/v1/get-event/uuid>;rel=\"self\"]", result.toString());
        assertNotNull(result.getId());
        assertEquals(entity.getEventName(), result.getEventName());
        assertEquals(entity.getCep(), result.getCep());
        assertEquals(entity.getDateTime(), result.getDateTime());
        assertEquals(entity.getCidade(), result.getCidade());
        assertEquals(entity.getUf(), result.getUf());
        assertEquals(entity.getLogradouro(), result.getLogradouro());
        assertEquals(false, entity.getDeleted());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getDeletedAt());

    }

    @Test
    void createEventWithInvalidCepThrowsException() {
        EventRequestDto entity = event.mockEventCreateDto();
        when(viaCepService.getCepInfo(anyString())).thenThrow(FeignException.NotFound.class);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.create(entity); // Deve lançar exceção
        });

        assertEquals("Server could not find cep", exception.getMessage());

    }

    @Test
    void updateEvent() {
        Event entity = event.mockEvent();

        when(repository.save(any(Event.class))).thenReturn(entity);
        when(viaCepService.getCepInfo(anyString())).thenReturn(new ViaCep());
        when(repository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(entity));
        EventRequestDto dto = event.mockEventCreateDto();

        var result = service.update(dto, "uuid");

        assertNotNull(result);
        assertEquals("links: [</api/events/v1/get-event/uuid>;rel=\"self\"]", result.toString());
        assertNotNull(result.getId());
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getEventName(), result.getEventName());
        assertEquals(entity.getCep(), result.getCep());
        assertEquals(entity.getDateTime(), result.getDateTime());
        assertEquals(entity.getCidade(), result.getCidade());
        assertEquals(entity.getUf(), result.getUf());
        assertEquals(entity.getLogradouro(), result.getLogradouro());
    }

    @Test
    void updateEventWithInvalidIdThrowsException() {
        EventRequestDto entity = event.mockEventCreateDto();
        String id = "uuid";
        when(repository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.update(entity, id);
        });

        assertEquals("Event with id " + id + " not found", exception.getMessage());

    }

    @Test
    void updateEventWithInvalidCepThrowsException() {
        Event entity = event.mockEvent();
        String id = "uuid";
        when(repository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(entity));


        var dto = event.mockEventCreateDto();

        when(viaCepService.getCepInfo(anyString())).thenThrow(FeignException.NotFound.class);

        Exception exception = assertThrows(CepNotFoundException.class, () -> {
            service.update(dto, id); // Deve lançar exceção
        });

        assertEquals("Server could not find cep", exception.getMessage());

    }

    @Test
    void findEvent() {
        Event entity = event.mockEvent();
        when(repository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(entity));

        var result = service.findById(entity.getId());

        assertNotNull(result);
        assertEquals("links: [</api/events/v1/get-event/uuid>;rel=\"self\"]", result.toString());
        assertNotNull(result.getId());
        assertEquals(entity.getEventName(), result.getEventName());
        assertEquals(entity.getCep(), result.getCep());
        assertEquals(entity.getDateTime(), result.getDateTime());
        assertEquals(entity.getCidade(), result.getCidade());
        assertEquals(entity.getUf(), result.getUf());
        assertEquals(entity.getLogradouro(), result.getLogradouro());
    }

    @Test
    void findEventWithInvalidIdThrowsException() {
        Event entity = event.mockEvent();
        when(repository.findByIdAndIsDeletedFalse(anyString())).thenThrow( new EventNotFoundException("Event with id " + entity.getId() + " not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
           service.findById(entity.getId());
        });

        assertEquals("Event with id " + entity.getId() + " not found", exception.getMessage());
    }

    @Test
    void findAll() {
        List<Event> events = event.mockEventList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> page = new PageImpl<>(events, pageable, events.size());

        when(repository.findByIsDeletedFalse(pageable)).thenReturn(page);

        var result = service.findAll(pageable);
        assertNotNull(result);
        assertEquals(events.size(), result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(events.size(), result.getContent().size());

        for (int i =0 ; i < events.size() ; i++) {
            assertEquals(events.get(i).getEventName(), result.getContent().get(i).getEventName());
            assertEquals(events.get(i).getCep(), result.getContent().get(i).getCep());
            assertEquals(events.get(i).getDateTime(), result.getContent().get(i).getDateTime());
            assertEquals(events.get(i).getCidade(), result.getContent().get(i).getCidade());
            assertEquals(events.get(i).getUf(), result.getContent().get(i).getUf());
            assertEquals(events.get(i).getLogradouro(), result.getContent().get(i).getLogradouro());
        }
    }

    @Test
    void findAllSorted() {
        List<Event> events = event.mockEventList();
        Sort sort = Sort.by(Sort.Direction.ASC, "eventName");
        Pageable pageable = PageRequest.of(0, 10, sort  );
        events.sort((e1, e2) -> e1.getEventName().compareTo(e2.getEventName()));

        Page<Event> page = new PageImpl<>(events, pageable, events.size());

        when(repository.findByIsDeletedFalse(pageable)).thenReturn(page);

        var result = service.findAllSorted(pageable);

        assertNotNull(result);
        assertEquals(events.size(), result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(events.size(), result.getContent().size());

        for (int i =0 ; i < events.size() ; i++) {
            assertEquals(events.get(i).getEventName(), result.getContent().get(i).getEventName());
            assertEquals(events.get(i).getCep(), result.getContent().get(i).getCep());
            assertEquals(events.get(i).getDateTime(), result.getContent().get(i).getDateTime());
            assertEquals(events.get(i).getCidade(), result.getContent().get(i).getCidade());
            assertEquals(events.get(i).getUf(), result.getContent().get(i).getUf());
            assertEquals(events.get(i).getLogradouro(), result.getContent().get(i).getLogradouro());
        }
    }

    @Test
    void deleteEvent() {
        Event entity = event.mockEvent();
        when(repository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(entity));
        when(ticketService.checkTicketsByEventId(anyString())).thenReturn(ticket.mockEmptyPagedModelTicket());

        service.delete(entity.getId());
    }

    @Test
    void deleteEventWithTicketsAssociateThrowsException() {
        Event entity = event.mockEvent();

        when(repository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(entity));
        when(ticketService.checkTicketsByEventId(anyString())).thenThrow(new TicketsAssociatedWithEventException("There are tickets associated with this event"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.delete(entity.getId());
        });

        assertEquals("There are tickets associated with this event", exception.getMessage());
    }

    @Test
    void deleteEventWithInvalidIdThrowsException() {
        String id = "uuid";
        when(repository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EventNotFoundException.class, () -> {
            service.delete(id);
        });

        assertEquals("Event with id " + id + " not found", exception.getMessage());
    }

}
