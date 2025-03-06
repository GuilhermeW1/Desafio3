package org.example.events.controller;

import org.example.events.dto.EventCreateDto;
import org.example.events.dto.EventResponseDto;
import org.example.events.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eventmanagement/v1")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDto> create(@RequestBody EventCreateDto event) {
        return ResponseEntity.ok(new EventResponseDto());
    }
}
