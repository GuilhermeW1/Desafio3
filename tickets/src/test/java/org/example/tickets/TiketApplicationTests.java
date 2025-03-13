package org.example.tickets;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class TiketApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainTest() {
        assertDoesNotThrow(() -> TicketsApplication.main(new String[0]));
    }
}
