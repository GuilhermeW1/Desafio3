package org.example.tiket.service;

import org.example.tiket.entity.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Event", url = "http://localhost:8080/api/events/v1")
public interface EventService {

    @GetMapping("/get-event/{id}")
    Event getEvent(@PathVariable String id);
}
