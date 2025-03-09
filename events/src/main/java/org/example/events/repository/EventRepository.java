package org.example.events.repository;

import org.example.events.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findByIdAndIsDeletedFalse(String id);
    Page<Event> findByIsDeletedFalse(Pageable pageable);
    Page<Event> findByIsDeletedFalse(Sort sort, Pageable pageable);
}
