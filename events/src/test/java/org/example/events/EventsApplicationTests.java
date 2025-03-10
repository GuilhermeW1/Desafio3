package org.example.events;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class EventsApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void tesMain() {
        assertDoesNotThrow(() -> EventsApplication.main(new String[]{}));
    }
}
