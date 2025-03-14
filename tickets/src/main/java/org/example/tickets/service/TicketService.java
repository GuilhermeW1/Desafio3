package org.example.tickets.service;

import feign.FeignException;
import org.example.tickets.controller.TicketController;
import org.example.tickets.dto.TicketRequestDto;
import org.example.tickets.dto.TicketResponseDto;
import org.example.tickets.dto.mappers.TicketMapper;
import org.example.tickets.entity.Event;
import org.example.tickets.entity.Ticket;
import org.example.tickets.exceptions.EventNotFoundException;
import org.example.tickets.exceptions.TicketNotFoundException;
import org.example.tickets.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventService eventService;

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    public TicketResponseDto findById(String id) {
        logger.info("Finding ticket by id: {}", id);
        Ticket ticket = ticketRepository.findByTicketIdAndIsDeletedFalse(id).orElseThrow(() -> {
            logger.warn("Ticket with id {} not found", id);
            return new TicketNotFoundException("Ticket with id " + id + " not found");
        });
        var dto = TicketMapper.toDto(ticket);
        dto.add(linkTo(methodOn(TicketController.class).getTicket(dto.getTicketId())).withSelfRel());
        return dto;
    }

    public Page<TicketResponseDto> findByCpf(String cpf, Pageable pageable) {
        logger.info("Finding tickets by cpf");
        Page<Ticket> tickets = ticketRepository.findByCpfAndIsDeletedFalse(cpf, pageable);
        var dtos = tickets.map(TicketMapper::toDto);
        dtos.map(e -> e.add(linkTo(methodOn(TicketController.class).getTicket(e.getTicketId())).withSelfRel()));
        return dtos;
    }

    public TicketResponseDto create(TicketRequestDto ticketRequestDto) {
        logger.info("Creating ticket");
        Ticket ticket = new Ticket();
        Event event;

        try {
            event = eventService.getEvent(ticketRequestDto.getEventId());
        } catch (FeignException.NotFound e) {
            logger.error("Error while creating ticket event with id " + ticketRequestDto.getEventId() + " not found");
            throw new EventNotFoundException("Event with id " + ticketRequestDto.getEventId() + " not found");
        }

        String uuid = UUID.randomUUID().toString();
        ticket.setTicketId(uuid);

        ticket.setCustomerName(ticketRequestDto.getCustomerName());
        ticket.setCustomerMail(ticketRequestDto.getCustomerMail());
        ticket.setEvent(event);
        ticket.setCpf(ticketRequestDto.getCpf());
        ticket.setBRLamount(ticketRequestDto.getBrlAmount());
        ticket.setUSDamount(ticketRequestDto.getUsdAmount());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(null);
        ticket.setDeletedAt(null);
        ticket.setDeleted(false);
        ticket.setStatus("COMPLETED");
        ticket = ticketRepository.save(ticket);
        var dto = TicketMapper.toDto(ticket);
        dto.add(linkTo(methodOn(TicketController.class).getTicket(dto.getTicketId())).withSelfRel());
        return dto;
    }

    public TicketResponseDto update(TicketRequestDto ticketRequestDto, String id) {
        logger.info("Updating ticket");
        Ticket ticket = ticketRepository.findByTicketIdAndIsDeletedFalse(id).orElseThrow(() -> {
            logger.error("Error while updating, ticket id {} not found", id);
            return new TicketNotFoundException("Ticket with id " + id + " not found");
        });
        Event event;
        try {
            event = eventService.getEvent(ticketRequestDto.getEventId());
        } catch (FeignException.NotFound e) {
            logger.error("Error while updating ticket, event id not found");
            throw new EventNotFoundException("Event with id " + ticketRequestDto.getEventId() + " not found");
        }

        ticket.setCustomerName(ticketRequestDto.getCustomerName());
        ticket.setCustomerMail(ticketRequestDto.getCustomerMail());
        ticket.setEvent(event);
        ticket.setCpf(ticketRequestDto.getCpf());
        ticket.setBRLamount(ticketRequestDto.getBrlAmount());
        ticket.setUSDamount(ticketRequestDto.getUsdAmount());
        ticket.setUpdatedAt(LocalDateTime.now());

        ticket = ticketRepository.save(ticket);
        var dto = TicketMapper.toDto(ticket);
        dto.add(linkTo(methodOn(TicketController.class).getTicket(dto.getTicketId())).withSelfRel());
        return dto;
    }

    public void delete(String id) {
        logger.info("Deleting ticket by id");
        Ticket ticket = ticketRepository.findByTicketIdAndIsDeletedFalse(id).orElseThrow(() -> {
            logger.error("Error while deleting ticket, ticket id not found");
            return new TicketNotFoundException("Ticket with id " + id + " not found");
        });
        ticket.setDeleted(true);
        ticket.setDeletedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    public Page<TicketResponseDto> findAllByEventId(String eventId, Pageable pageable) {
        logger.info("Finding all events by event id");
        Page<Ticket> tickets = ticketRepository.findAllByEvent_IdAndIsDeletedFalse(eventId, pageable);
        var dtos = tickets.map(TicketMapper::toDto);
        dtos.map(e -> e.add(linkTo(methodOn(TicketController.class).getTicket(e.getTicketId())).withSelfRel()));
        return dtos;
    }
}
