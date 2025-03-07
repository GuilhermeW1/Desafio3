package org.example.tiket.repository;

import org.example.tiket.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findByAndIsDeletedFalse(String id);
    List<Ticket> findAllByIsDeletedFalse();

}
