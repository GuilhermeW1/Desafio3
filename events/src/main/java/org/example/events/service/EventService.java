package org.example.events.service;

import org.example.events.dto.EventRequestDto;
import org.example.events.dto.EventResponseDto;
import org.example.events.dto.mappers.EventMapper;
import org.example.events.entity.Event;
import org.example.events.entity.Ticket;
import org.example.events.entity.ViaCep;
import org.example.events.exceptions.CepNotFoundException;
import org.example.events.exceptions.EventNotFoundException;
import org.example.events.exceptions.TicketsAssociatedWithEventException;
import org.example.events.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private TicketService ticketService;

    public EventResponseDto create(EventRequestDto eventCreateDto) {
        Event event = new Event();
        String uuid = UUID.randomUUID().toString();
        ViaCep viaCep = new ViaCep();

        try {
            viaCep = viaCepService.getCepInfo(eventCreateDto.getCep());
        } catch (Exception e) {
            throw new CepNotFoundException("Server could not find cep");
        }

        event.setId(uuid);
        event.setEventName(eventCreateDto.getEventName());
        event.setDateTime(eventCreateDto.getDateTime());
        event.setCep(eventCreateDto.getCep());
        event.setBairro(viaCep.getBairro());
        event.setCidade(viaCep.getLocalidade());
        event.setUf(viaCep.getUf());
        event.setLogradouro(viaCep.getLogradouro());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(null);
        event.setDeletedAt(null);
        event.setDeleted(false);

        event = eventRepository.save(event);

        return EventMapper.toDto(event);
    }

    public EventResponseDto findById(String id) {
        Event event = eventRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));
        return EventMapper.toDto(event);
    }

    public List<EventResponseDto> findAll() {
        List<Event> events = eventRepository.findByIsDeletedFalse();
        return EventMapper.toListDto(events);
    }

    public List<EventResponseDto> findAllSorted() {
        List<Event> events = eventRepository.findByIsDeletedFalse(Sort.by(Sort.Direction.ASC, "eventName")) ;
        return EventMapper.toListDto(events);
    }

    public EventResponseDto update(EventRequestDto eventCreateDto, String id) {
        Event event = new Event();
        ViaCep viaCep;

        var old = eventRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));

        try {
            viaCep = viaCepService.getCepInfo(eventCreateDto.getCep());
        } catch (Exception e) {
            throw new CepNotFoundException("Server could not find cep");
        }

        event.setId(id);
        event.setEventName(eventCreateDto.getEventName());
        event.setDateTime(eventCreateDto.getDateTime());
        event.setCep(eventCreateDto.getCep());

        event.setBairro(viaCep.getBairro());
        event.setCidade(viaCep.getLocalidade());
        event.setUf(viaCep.getUf());
        event.setLogradouro(viaCep.getLogradouro());
        event.setCreatedAt(old.getCreatedAt());
        event.setUpdatedAt(LocalDateTime.now());
        event.setDeletedAt(null);
        event.setDeleted(false);

        event = eventRepository.save(event);

        return EventMapper.toDto(event);
    }

    public void delete(String id) {
        Event event = eventRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));
        //todo change checkTickets name to checkTicketsByEventId
        List<Ticket> tickets = ticketService.checkTickets(id);
        if (!tickets.isEmpty()) {
            throw new TicketsAssociatedWithEventException("There are tickets associated with this event");
        }
        event.setDeletedAt(LocalDateTime.now());
        event.setDeleted(true);
        eventRepository.save(event);
    }

}
