package org.example.events.dto.mappers;

import org.example.events.dto.EventResponseDto;
import org.example.events.entity.Event;
import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {



    public static EventResponseDto toDto(Event event) {
        return new ModelMapper().map(event, EventResponseDto.class);
    }

    public static List<EventResponseDto> toListDto(List<Event> events) {
        return events.stream().map(EventMapper::toDto).collect(Collectors.toList());
    }

//    public static Event toEvent(EventResponseDto dto) {
//        return new ModelMapper().map(dto, Event.class);
//    }
}
