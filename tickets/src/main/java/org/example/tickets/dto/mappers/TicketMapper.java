package org.example.tickets.dto.mappers;

import org.example.tickets.dto.TicketResponseDto;
import org.example.tickets.entity.Ticket;
import org.modelmapper.ModelMapper;

public class TicketMapper {
    public static TicketResponseDto toDto(Ticket ticket) {
        return new ModelMapper().map(ticket, TicketResponseDto.class);
    }
}
