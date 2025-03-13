package org.example.events.service;

import feign.FeignException;
import org.example.events.controller.EventController;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ViaCepService viaCepService;

    @Autowired
    private TicketService ticketService;

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    public EventResponseDto create(EventRequestDto eventCreateDto) {
        logger.info("Creating event");
        Event event = new Event();
        String uuid = UUID.randomUUID().toString();
        ViaCep viaCep;

        try {
            viaCep = viaCepService.getCepInfo(eventCreateDto.getCep());
        } catch (FeignException.NotFound e) {
            logger.error("Error while creating event, Cep not found");
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
        var dto = EventMapper.toDto(event);
        dto.add(linkTo(methodOn(EventController.class).getEvent(event.getId())).withSelfRel());
        return dto;
    }

    public EventResponseDto findById(String id) {
        logger.info("Finding event by id");
        Event event = eventRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> {
            logger.error("Error finding event by id, Event with id {} not found", id);
            return new EventNotFoundException("Event with id " + id + " not found");
        });
        var dto = EventMapper.toDto(event);
        dto.add(linkTo(methodOn(EventController.class).getEvent(event.getId())).withSelfRel());
        return dto;
    }

    public Page<EventResponseDto> findAll(Pageable pageable) {
        logger.info("Finding all events");
        Page<Event> events = eventRepository.findByIsDeletedFalse(pageable);
        return events.map(EventMapper::toDto);

    }

    public Page<EventResponseDto> findAllSorted(Pageable pageable) {
        logger.info("Finding all sorted events");
        Page<Event> events = eventRepository.findByIsDeletedFalse(pageable) ;
        return events.map(EventMapper::toDto);
    }

    public EventResponseDto update(EventRequestDto eventCreateDto, String id) {
        logger.info("Updating event");
        ViaCep viaCep;
        Event event = eventRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> {
            logger.error("Error while updating event, Event id not found");
            return new EventNotFoundException("Event with id " + id + " not found");
        });

        try {
            viaCep = viaCepService.getCepInfo(eventCreateDto.getCep());
        } catch (FeignException.NotFound e) {
            logger.error("Error while updating event, Cep not found");
            throw new CepNotFoundException("Server could not find cep");
        }

        event.setEventName(eventCreateDto.getEventName());
        event.setDateTime(eventCreateDto.getDateTime());
        event.setCep(eventCreateDto.getCep());

        event.setBairro(viaCep.getBairro());
        event.setCidade(viaCep.getLocalidade());
        event.setUf(viaCep.getUf());
        event.setLogradouro(viaCep.getLogradouro());
        event.setUpdatedAt(LocalDateTime.now());

        event = eventRepository.save(event);

        var dto = EventMapper.toDto(event);
        dto.add(linkTo(methodOn(EventController.class).getEvent(event.getId())).withSelfRel());
        return dto;
    }

    public void delete(String id) {
        logger.info("Deleting event");
        Event event = eventRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> {
            logger.error("Error while deleting event, Event id not found");
            return new EventNotFoundException("Event with id " + id + " not found");
        });
        PagedModel<EntityModel<Ticket>> tickets = ticketService.checkTicketsByEventId(id);
        if (!tickets.getContent().isEmpty()) {
            throw new TicketsAssociatedWithEventException("There are tickets associated with this event");
        }
        event.setDeletedAt(LocalDateTime.now());
        event.setDeleted(true);
        eventRepository.save(event);
    }

}
