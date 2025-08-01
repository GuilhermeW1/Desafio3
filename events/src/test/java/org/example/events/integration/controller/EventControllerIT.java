package org.example.events.integration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.example.events.dto.EventRequestDto;
import org.example.events.entity.Event;
import org.example.events.entity.ViaCep;
import org.example.events.exceptions.CepNotFoundException;
import org.example.events.exceptions.EventNotFoundException;
import org.example.events.exceptions.ExceptionResponse;
import org.example.events.integration.AbstractTest;
import org.example.events.moks.*;
import org.example.events.repository.EventRepository;
import org.example.events.service.TicketService;
import org.example.events.service.ViaCepService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerIT extends AbstractTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    ViaCepService viaCepService;

    @MockitoBean
    EventRepository eventRepository;

    @MockitoBean
    TicketService ticketService;

    static List<Event> events = new MockEvent().mockEventList();
    private static ObjectMapper objectMapper;
    private static MockTicket ticket;


    @BeforeAll
    static void insertTestData(@Autowired MongoTemplate mongoTemplate) {
        for (Event event : events) {
            mongoTemplate.save(event);
        }
        ticket = new MockTicket();
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    void testCreateEvent() {
        ViaCep cep = new MockCep().mockViaCep();
        Event event = new MockEvent().mockEvent();
        when(viaCepService.getCepInfo(anyString())).thenReturn(cep);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventRequestDto requestDto = new EventRequestDto();
        requestDto.setCep(event.getCep());
        requestDto.setEventName(event.getEventName());
        requestDto.setDateTime(event.getDateTime());
        var test  = given()
                .basePath("api/events/v1/create-event")
                    .port(port)
                    .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(EventDtoJackson.class);

        assertEquals(requestDto.getEventName(), test.getEventName());
        assertEquals(requestDto.getCep(), test.getCep());
        assertNotNull(test.getLinks());
    }

    @Test
    void testCreteEvent_withInvalidCep_throwsException404() {
        when(viaCepService.getCepInfo(anyString())).thenThrow(FeignException.NotFound.class);

        EventRequestDto requestDto = new MockEvent().mockEventCreateDto();
        given()
                .basePath("api/events/v1/create-event")
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .post()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void createEvent_withInvalidBody_throwsException422() {
        EventRequestDto requestDto = new MockEvent().mockEventCreateDto();

        requestDto.setEventName("1");

        given()
                .basePath("api/events/v1/create-event")
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .post()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        requestDto.setEventName("valid");
        requestDto.setCep("");

        given()
                .basePath("api/events/v1/update-event/" + "valid")
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        requestDto.setEventName("valid");
        requestDto.setCep("95948000");
        requestDto.setDateTime(null);

        given()
                .basePath("api/events/v1/update-event/" + "valid")
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void findEventById() {
        Event event = events.get(0);
        when(eventRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(event));
        var test = given()
                .basePath("api/events/v1/get-event/"+event.getId())
                    .port(port)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .extract()
                        .body()
                        .as(EventDtoJackson.class);

        assertNotNull(test);
        assertEquals(event.getEventName(), test.getEventName());
        assertNotNull(test.getDateTime()); //have to round the time
        assertEquals(event.getId(), test.getId());
        assertEquals(event.getCep(), test.getCep());
    }

    @Test
    void findEventById_withInvalidId_throwsException404() {
        given()
                .basePath("api/events/v1/get-event/"+ "invalid-id")
                .port(port)
                .when()
                .get()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void getAllEvents() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> page = new PageImpl<>(events, pageable, events.size());
        when(eventRepository.findByIsDeletedFalse(any(Pageable.class))).thenReturn(page);

        var test = given()
                .basePath("api/events/v1/get-all-events")
                .port(port)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var res = objectMapper.readValue(test, WrapperDto.class);
        var dtos = res.getListDto().getEventResponseDtoList();

        assertEquals(dtos.size(), events.size());
        assertNotNull(res.getLinks().getSelf().getHref());
        for (int i = 0; i < events.size(); i++) {
            assertEquals(events.get(i).getEventName(), dtos.get(i).getEventName());
            assertNotNull(dtos.get(i).getDateTime());//have to round the time
            assertEquals(events.get(i).getId(), dtos.get(i).getId());
            assertEquals(events.get(i).getCep(), dtos.get(i).getCep());
            assertEquals(events.get(i).getLogradouro(), dtos.get(i).getLogradouro());
            assertEquals(events.get(i).getUf(), dtos.get(i).getUf());
            assertEquals(events.get(i).getCidade(), dtos.get(i).getCidade());
        }
    }

    @Test
    void getAllSortedEvents() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "eventName"));
        Page<Event> page = new PageImpl<>(events, pageable, events.size());
        when(eventRepository.findByIsDeletedFalse(any(Pageable.class))).thenReturn(page);

        var test = given()
                .basePath("api/events/v1/get-all-events/sorted")
                .port(port)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        var dto = objectMapper.readValue(test, WrapperDto.class);
        var arr = dto.getListDto().getEventResponseDtoList();
        assertEquals(arr.size(), events.size());
        assertNotNull(dto.getLinks().getSelf().getHref());


        for (int i = 0; i < events.size(); i++) {
            assertEquals(events.get(i).getEventName(), arr.get(i).getEventName());
            assertNotNull(arr.get(i).getDateTime());
            assertEquals(events.get(i).getId(), arr.get(i).getId());
            assertEquals(events.get(i).getCep(), arr.get(i).getCep());
            assertEquals(events.get(i).getLogradouro(), arr.get(i).getLogradouro());
            assertEquals(events.get(i).getUf(), arr.get(i).getUf());
            assertEquals(events.get(i).getCidade(), arr.get(i).getCidade());
        }
    }

    @Test
    void updateEvent() {
        EventRequestDto req = new EventRequestDto();

        Event replaceEvent = events.get(0);
        Event event2 = events.get(1);

        req.setEventName(replaceEvent.getEventName());
        req.setCep(replaceEvent.getCep());
        req.setDateTime(replaceEvent.getDateTime());

        ViaCep cep = new MockCep().mockViaCep();

        when(viaCepService.getCepInfo(anyString())).thenReturn(cep);

        when(eventRepository.save(any(Event.class))).thenReturn(replaceEvent);

        when(eventRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(replaceEvent));

        var newEvent2 = given()
                .basePath("api/events/v1/update-event/"+event2.getId())
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(req)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(EventDtoJackson.class);
        System.out.println(newEvent2.toString());

        assertEquals(newEvent2.getEventName(), replaceEvent.getEventName());
        assertEquals(newEvent2.getCep(), replaceEvent.getCep());
        assertNotNull(replaceEvent.getDateTime());
    }

    @Test
    void updateEvent_withInvalidCep_throwsException404() {
        when(eventRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(events.get(0)));
        when(viaCepService.getCepInfo(anyString())).thenThrow(FeignException.NotFound.class);
        Event event = events.get(0);
        EventRequestDto requestDto = new MockEvent().mockEventCreateDto();
        ExceptionResponse ex = given()
                .basePath("api/events/v1/update-event/"+ event.getId() )
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .put()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void updateEvent_withInvalidId_throwsException404() {
        when(eventRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        EventRequestDto requestDto = new MockEvent().mockEventCreateDto();
        given()
                .basePath("api/events/v1/update-event/" + "invalid")
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .put()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void updateEvent_withInvalidBody_throwsException422() {
        EventRequestDto requestDto = new MockEvent().mockEventCreateDto();

        requestDto.setEventName("1");

        given()
                .basePath("api/events/v1/update-event/" + "invalid")
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        requestDto.setEventName("valid");
        requestDto.setCep("");

        given()
                .basePath("api/events/v1/update-event/" + "valid")
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        requestDto.setEventName("valid");
        requestDto.setCep("95948000");
        requestDto.setDateTime(null);

        given()
                .basePath("api/events/v1/update-event/" + "valid")
                .port(port)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(requestDto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }


    @Test
    void deleteEvent() {
        Event event = events.get(0);

        when(eventRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(event));
        when(ticketService.checkTicketsByEventId(anyString())).thenReturn(ticket.mockEmptyPagedModelTicket());

        given()
                .basePath("api/events/v1/delete-event/" + event.getId())
                .port(port)
                .when()
                .delete()
                .then()
                .statusCode(204)
                .extract()
                .body();
    }

    @Test
    void deleteEvent_withTicketsAssociate_throwsException409() {
        Event event = events.get(0);

        when(eventRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(event));
        when(ticketService.checkTicketsByEventId(anyString())).thenReturn(ticket.mockPagedModelTicket());

        given()
                .basePath("api/events/v1/delete-event/" + event.getId())
                .port(port)
                .when()
                .delete()
                .then()
                .statusCode(409)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void deleteEvent_withInvalidEventId_throwsException404() {
        Event event = events.get(0);
        when(eventRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        given()
                .basePath("api/events/v1/delete-event/" + event.getId())
                .port(port)
                .when()
                .delete()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void testError500() {
        Event event = events.get(0);
        when(eventRepository.findByIdAndIsDeletedFalse(anyString())).thenThrow(new RuntimeException("Server could not find event"));

        given()
                .basePath("api/events/v1/delete-event/" + event.getId())
                .port(port)
                .when()
                .delete()
                .then()
                .statusCode(500)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }
}
