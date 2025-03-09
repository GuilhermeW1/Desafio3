package org.example.tiket.controller;

import jakarta.validation.Valid;
import org.example.tiket.dto.TicketRequestDto;
import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/tickets/v1")
@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponseDto> createTicket(@RequestBody @Valid TicketRequestDto ticketRequestDto) {
        return ResponseEntity.ok().body(ticketService.create(ticketRequestDto));
    }

    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketResponseDto> getTicket(@PathVariable String id) {
        return ResponseEntity.ok().body(ticketService.findById(id));
    }

    @GetMapping("/get-ticket-by-cpf/{cpf}")
    public ResponseEntity<List<TicketResponseDto>> getTicketByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok().body(ticketService.findByCpf(cpf));
    }

    @PutMapping("/update-ticket/{id}")
    public ResponseEntity<TicketResponseDto> updateTicket(@PathVariable String id, @RequestBody @Valid TicketRequestDto ticketRequestDto) {
        return ResponseEntity.ok().body(ticketService.update(ticketRequestDto, id));
    }

    @DeleteMapping("/cancel-ticket/{id}")
    public ResponseEntity<TicketResponseDto> cancelTicket(@PathVariable String id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-tickets-by-event/{eventId}")
    public ResponseEntity<List<TicketResponseDto>> checkTicketsByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok().body(ticketService.findAllByEventId(eventId));
    }
}
