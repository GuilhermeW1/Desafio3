package org.example.tiket.unittests;

import org.example.tiket.entity.Event;
import org.example.tiket.moks.MockEvent;
import org.example.tiket.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    EventService eventService;

    @Test
    public void eventServiceTest() {
        Event event = new MockEvent().mockEvent();
        when(eventService.getEvent(anyString())).thenReturn(event);
        var result = eventService.getEvent("ticketId");

        assertEquals(event.getId(), result.getId());
        assertEquals(event.getEventName(), result.getEventName());
        assertEquals(event.getBairro(), result.getBairro());
        assertEquals(event.getCidade(), result.getCidade());
        assertEquals(event.getLogradouro(), result.getLogradouro());
        assertEquals(event.getUf(), result.getUf());
        assertEquals(event.getDateTime(), result.getDateTime());
    }
}
