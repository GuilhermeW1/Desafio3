package org.example.events.unittests;

import org.example.events.moks.MockTicket;
import org.example.events.service.TicketService;
import org.example.events.moks.MockEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    MockEvent input;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    void init() {
        this.input = new MockEvent();
    }


    @Test
    void getTicketsByEventId() {
        when(ticketService.checkTicketsByEventId(anyString())).thenReturn(new MockTicket().mockPagedModelTicket());
        var result = ticketService.checkTicketsByEventId("teste");
        var arr = result.getContent();
        assertNotNull(result);
        assertEquals(1, arr.size());
    }

}
