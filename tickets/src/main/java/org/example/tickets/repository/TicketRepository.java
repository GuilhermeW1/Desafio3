package org.example.tickets.repository;

import org.example.tickets.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    Page<Ticket> findByCpfAndIsDeletedFalse(String cpf, Pageable pageable);

    Page<Ticket> findAllByEvent_IdAndIsDeletedFalse(String eventId, Pageable pageable);

    Optional<Ticket> findByTicketIdAndIsDeletedFalse(String id);
}
