package org.example.events.service;

import org.example.events.dto.EventCreateDto;
import org.example.events.dto.EventResponseDto;
import org.example.events.dto.mappers.EventMapper;
import org.example.events.entity.Event;
import org.example.events.entity.ViaCep;
import org.example.events.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ViaCepService viaCepService;

    public EventResponseDto create(EventCreateDto eventCreateDto) {
        Event event = new Event();
        String uuid = UUID.randomUUID().toString();

        ViaCep viaCep = viaCepService.getCepInfo(eventCreateDto.getCep());

        event.setId(uuid);
        event.setEventName(eventCreateDto.getEventName());
        event.setDateTime(eventCreateDto.getDateTime());
        event.setCep(eventCreateDto.getCep());
        event.setBairro(viaCep.getBairro());
        event.setCidade(viaCep.getLocalidade());
        event.setUf(viaCep.getUf());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        event.setDeletedAt(null);
        event.setDeleted(false);

        event = eventRepository.save(event);

        return EventMapper.toDto(event);
    }

    public EventResponseDto findById(String id) {
        Event event = eventRepository.findById(id).orElse(null);
        return EventMapper.toDto(event);
    }

    public List<EventResponseDto> findAll() {
        List<Event> events = eventRepository.findAll();
        return EventMapper.toListDto(events);
    }

    public EventResponseDto update(EventCreateDto eventCreateDto) {
        return new EventResponseDto();
    }

    public void delete(String id) {

    }

}
