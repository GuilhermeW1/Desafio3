package org.example.tiket.service;

import org.example.tiket.dto.TicketRequestDto;
import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.dto.mappers.TicketMapper;
import org.example.tiket.entity.Event;
import org.example.tiket.entity.Ticket;
import org.example.tiket.exceptions.CpfNotFoundException;
import org.example.tiket.exceptions.EventNotFoundException;
import org.example.tiket.exceptions.TicketNotFoundException;
import org.example.tiket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventService eventService;

    public TicketResponseDto findById(String id) {
        Ticket ticket = ticketRepository.findByTicketIdAndIsDeletedFalse(id).orElseThrow(
                () -> new TicketNotFoundException("Ticket with id " + id + " not found")
        );
        return TicketMapper.toDto(ticket);
    }

    public Page<TicketResponseDto> findByCpf(String cpf, Pageable pageable) {
        Page<Ticket> tickets = ticketRepository.findByCpfAndIsDeletedFalse(cpf, pageable);
        return tickets.map(TicketMapper::toDto);
    }

    public TicketResponseDto create(TicketRequestDto ticketRequestDto) {
        Ticket ticket = new Ticket();
        Event event;

        try {
            event = eventService.getEvent(ticketRequestDto.getEventId());
        } catch (Exception e) {
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
        ticket.setStatus("CREATED");
        ticket = ticketRepository.save(ticket);
        return TicketMapper.toDto(ticket);
    }

    public TicketResponseDto update(TicketRequestDto ticketRequestDto, String id) {

        Ticket ticket = ticketRepository.findByTicketIdAndIsDeletedFalse(id).orElseThrow(
                () -> new TicketNotFoundException("Ticket with id " + id + " not found")
        );
        Event event;
        try {
            event = eventService.getEvent(ticketRequestDto.getEventId());
        } catch (Exception e) {
            throw new EventNotFoundException("Event with id " + ticketRequestDto.getEventId() + " not found");
        }

        ticket.setCustomerName(ticketRequestDto.getCustomerName());
        ticket.setCustomerMail(ticketRequestDto.getCustomerMail());
        //todo: fazer a consulta do evento no servico
        ticket.setEvent(event);
        ticket.setCpf(ticketRequestDto.getCpf());
        ticket.setBRLamount(ticketRequestDto.getBrlAmount());
        ticket.setUSDamount(ticketRequestDto.getUsdAmount());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setDeletedAt(null);
        ticket.setDeleted(false);

        ticket = ticketRepository.save(ticket);
        return TicketMapper.toDto(ticket);
    }

    public void delete(String id) {
        Ticket ticket = ticketRepository.findByTicketIdAndIsDeletedFalse(id).orElseThrow(
                () -> new TicketNotFoundException("Ticket with id " + id + " not found")
        );
        ticket.setDeleted(true);
        ticket.setDeletedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    public Page<TicketResponseDto> findAllByEventId(String eventId, Pageable pageable) {
        Page<Ticket> tickets = ticketRepository.findAllByEvent_IdAndIsDeletedFalse(eventId, pageable);
        return tickets.map(TicketMapper::toDto);
    }
}
