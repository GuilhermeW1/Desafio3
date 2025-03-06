package org.example.events.controller;

import org.example.events.dto.EventRequestDto;
import org.example.events.dto.EventResponseDto;
import org.example.events.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/v1")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDto> create(@RequestBody EventRequestDto event) {
        return ResponseEntity.ok().body(eventService.create(event));
    }

    @GetMapping("/get-event/{id}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable String id) {
        return ResponseEntity.ok().body(eventService.findById(id));
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        return ResponseEntity.ok().body(eventService.findAll());
    }

    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<List<EventResponseDto>> getAllEventsSorted() {
        return ResponseEntity.ok().body(eventService.findAllSorted());
    }

    @PutMapping("update-event/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable String id, @RequestBody EventRequestDto event) {
        return ResponseEntity.ok().body(eventService.update(event, id));
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
