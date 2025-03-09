package org.example.events.controller;

import jakarta.validation.Valid;
import org.example.events.dto.EventRequestDto;
import org.example.events.dto.EventResponseDto;
import org.example.events.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/v1")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDto> create(@RequestBody @Valid EventRequestDto event) {
        return ResponseEntity.ok().body(eventService.create(event));
    }

    @GetMapping("/get-event/{id}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable String id) {
        return ResponseEntity.ok().body(eventService.findById(id));
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok().body(eventService.findAll(pageable));
    }

    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<Page<EventResponseDto>> getAllEventsSorted(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(dir, "eventName");
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok().body(eventService.findAllSorted(pageable));
    }

    @PutMapping("update-event/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable String id, @RequestBody @Valid EventRequestDto event) {
        return ResponseEntity.ok().body(eventService.update(event, id));
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
