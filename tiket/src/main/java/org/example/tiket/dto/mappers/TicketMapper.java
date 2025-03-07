package org.example.tiket.dto.mappers;

import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.entity.Ticket;
import org.modelmapper.ModelMapper;

public class TicketMapper {
    public static TicketResponseDto toDto(Ticket ticket) {
        return new ModelMapper().map(ticket, TicketResponseDto.class);
    }
}
