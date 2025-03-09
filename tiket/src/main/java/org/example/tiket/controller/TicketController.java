package org.example.tiket.controller;

import jakarta.validation.Valid;
import org.example.tiket.dto.TicketRequestDto;
import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<TicketResponseDto>> getTicketByCpf(
                @PathVariable String cpf,
                @RequestParam(value = "page", defaultValue = "0") Integer page,
                @RequestParam(value = "size", defaultValue = "3") Integer size
            ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok().body(ticketService.findByCpf(cpf, pageable));
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
    public ResponseEntity<Page<TicketResponseDto>> checkTicketsByEvent(
            @PathVariable String eventId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok().body(ticketService.findAllByEventId(eventId, pageable));
    }
}
