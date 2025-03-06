package org.example.events.unittests;

import org.example.events.dto.EventRequestDto;
import org.example.events.dto.EventResponseDto;
import org.example.events.entity.Event;
import org.example.events.entity.ViaCep;
import org.example.events.repository.EventRepository;
import org.example.events.service.EventService;
import org.example.events.service.ViaCepService;
import org.example.events.unittests.moks.MockEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private EventService service;

    @Mock
    private ViaCepService viaCepService;

    @Mock
    private EventRepository repository;

    @BeforeEach
    void setUp() {
        event = new MockEvent();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createEvent() {
        Event entity = event.mockEvent();

        when(repository.save(any(Event.class))).thenReturn(entity);
        when(viaCepService.getCepInfo(anyString())).thenReturn(new ViaCep());

        EventRequestDto dto = event.mockEventCreateDto();

        EventResponseDto result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(entity.getEventName(), result.getEventName());
        assertEquals(entity.getCep(), result.getCep());
        assertEquals(entity.getDateTime(), result.getDateTime());
        assertEquals(entity.getCidade(), result.getCidade());
        assertEquals(entity.getUf(), result.getUf());
        assertEquals(entity.getLogradouro(), result.getLogradouro());
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
    void findEvent() {
        Event entity = event.mockEvent();
        when(repository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(entity));

        var result = service.findById(entity.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(entity.getEventName(), result.getEventName());
        assertEquals(entity.getCep(), result.getCep());
        assertEquals(entity.getDateTime(), result.getDateTime());
        assertEquals(entity.getCidade(), result.getCidade());
        assertEquals(entity.getUf(), result.getUf());
        assertEquals(entity.getLogradouro(), result.getLogradouro());
    }

    @Test
    void findAll() {
        List<Event> events = event.mockEventList();

        when(repository.findByIsDeletedFalse()).thenReturn(events);

        var result = service.findAll();
        assertNotNull(result);
        assertEquals(result.size(), events.size());
        for (int i =0 ; i < events.size() ; i++) {
            assertEquals(events.get(i).getEventName(), result.get(i).getEventName());
            assertEquals(events.get(i).getCep(), result.get(i).getCep());
            assertEquals(events.get(i).getDateTime(), result.get(i).getDateTime());
            assertEquals(events.get(i).getCidade(), result.get(i).getCidade());
            assertEquals(events.get(i).getUf(), result.get(i).getUf());
            assertEquals(events.get(i).getLogradouro(), result.get(i).getLogradouro());
        }
    }
}
