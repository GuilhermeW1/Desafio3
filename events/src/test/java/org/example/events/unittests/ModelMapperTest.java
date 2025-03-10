package org.example.events.unittests;

import org.example.events.dto.EventResponseDto;
import org.example.events.dto.mappers.EventMapper;
import org.example.events.entity.Event;
import org.example.events.moks.MockEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ModelMapperTest {

    @Test
    void transformEventToEventResponseDto() {
        Event event = new MockEvent().mockEvent();
        EventResponseDto dto = EventMapper.toDto(event);

        assertNotNull(dto);
        assertEquals(dto.getId(), event.getId());
        assertEquals(dto.getCep(), event.getCep());
        assertEquals(dto.getEventName(), event.getEventName());
        assertEquals(dto.getDateTime(), event.getDateTime());
        assertEquals(dto.getUf(), event.getUf());
        assertEquals(dto.getCidade(), event.getCidade());
        assertEquals(dto.getLogradouro(), event.getLogradouro());
        assertEquals(dto.getBairro(), event.getBairro());
    }

    @Test
    void transformListEventIntoListEventResponseDto() {
        List<Event> events = new MockEvent().mockEventList();
        List<EventResponseDto> dtos = EventMapper.toListDto(events);

        assertNotNull(dtos);
        assertEquals(dtos.size(), events.size());
        for (int i = 0; i < events.size(); i++) {
            assertEquals(dtos.get(i).getId(), events.get(i).getId());
            assertEquals(dtos.get(i).getCep(), events.get(i).getCep());
            assertEquals(dtos.get(i).getEventName(), events.get(i).getEventName());
            assertEquals(dtos.get(i).getDateTime(), events.get(i).getDateTime());
            assertEquals(dtos.get(i).getUf(), events.get(i).getUf());
            assertEquals(dtos.get(i).getCidade(), events.get(i).getCidade());
            assertEquals(dtos.get(i).getLogradouro(), events.get(i).getLogradouro());
            assertEquals(dtos.get(i).getBairro(), events.get(i).getBairro());
        }
    }
}
