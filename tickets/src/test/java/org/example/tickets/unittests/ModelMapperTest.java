package org.example.tickets.unittests;

import org.example.tickets.dto.TicketResponseDto;
import org.example.tickets.dto.mappers.TicketMapper;
import org.example.tickets.entity.Ticket;
import org.example.tickets.moks.MockTicket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ModelMapperTest {



    @Test
    void ticketToDto() {
        Ticket ticket = new MockTicket().mockTicket();
        TicketResponseDto res = TicketMapper.toDto(ticket);

        assertEquals(res.getTicketId(), ticket.getTicketId());
        assertEquals(res.getUSDamount(), ticket.getUSDamount());
        assertEquals(res.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(res.getCustomerName(), ticket.getCustomerName());
        assertEquals(res.getStatus(), ticket.getStatus());
        assertEquals(res.getEvent(), ticket.getEvent());
        assertEquals(res.getBRLamount(), ticket.getBRLamount());
    }
}
