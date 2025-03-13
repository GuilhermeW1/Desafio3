package org.example.tiket.integration.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.example.tiket.dto.TicketRequestDto;
import org.example.tiket.dto.TicketResponseDto;
import org.example.tiket.entity.Event;
import org.example.tiket.entity.Ticket;
import org.example.tiket.exceptions.EventNotFoundException;
import org.example.tiket.exceptions.ExceptionResponse;
import org.example.tiket.exceptions.TicketNotFoundException;
import org.example.tiket.integration.AbstractTest;
import org.example.tiket.moks.MockEvent;
import org.example.tiket.moks.MockTicket;
import org.example.tiket.moks.TicketResponseDtoJackson;
import org.example.tiket.moks.WrapperDto;
import org.example.tiket.repository.TicketRepository;
import org.example.tiket.service.EventService;
import org.example.tiket.service.TicketService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketControllerIT extends AbstractTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    EventService eventService;

    @MockitoBean
    TicketRepository repository;

    static List<Ticket> tickets = new MockTicket().mockTicketList();
    private static ObjectMapper objectMapper;


    @BeforeAll
    static void insertTestData(@Autowired MongoTemplate mongoTemplate) {
        for (Ticket ticket : tickets) {
            mongoTemplate.save(ticket);
        }
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    MockTicket mockTicket;

    @BeforeEach
    void setUp() {
        mockTicket = new MockTicket();
    }

    @Test
    void createTicket() {
        Ticket ticket = mockTicket.mockTicket();
        Event event = new MockEvent().mockEvent();
        when(repository.save(any(Ticket.class))).thenReturn(ticket);
        when(eventService.getEvent(anyString())).thenReturn(event);

        TicketRequestDto dto = mockTicket.mockTicketCreateDto();

        var response = given()
                .basePath("api/tickets/v1/create-ticket")
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TicketResponseDtoJackson.class);

        assertNotNull(response);
        assertNotNull(response.getLinks().getSelf().getHref());
        assertEquals(response.getTicketId(), ticket.getTicketId());
        assertEquals(response.getEvent().getBairro(),  ticket.getEvent().getBairro());
        assertEquals(response.getEvent().getCidade(),  ticket.getEvent().getCidade());
        assertEquals(response.getEvent().getEventName(),  ticket.getEvent().getEventName());
        assertEquals(response.getEvent().getUf(),  ticket.getEvent().getUf());
        assertEquals(response.getEvent().getId(),  ticket.getEvent().getId());
        assertEquals(response.getEvent().getLogradouro(),  ticket.getEvent().getLogradouro());
        assertNotNull(ticket.getEvent().getDateTime());
        assertEquals(response.getStatus(), ticket.getStatus());
        assertEquals(response.getBRLamount(), ticket.getBRLamount());
        assertEquals(response.getUSDamount(), ticket.getUSDamount());
        assertEquals(response.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(response.getCustomerName(), ticket.getCustomerName());
    }

    @Test
    void createTicket_withInvalidEvent_throwsException404() {
        when(eventService.getEvent(anyString())).thenThrow(FeignException.NotFound.class);

        TicketRequestDto dto = mockTicket.mockTicketCreateDto();

        var response = given()
                .basePath("api/tickets/v1/create-ticket")
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void createTicket_withInvalidData_throwsException422() {
        Ticket ticket = mockTicket.mockTicket();
        Event event = new MockEvent().mockEvent();

        TicketRequestDto dto = mockTicket.mockTicketCreateDto();
        dto.setCustomerName("");

        var response = given()
                .basePath("api/tickets/v1/create-ticket")
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setEventName("");

        response = given()
                .basePath("api/tickets/v1/create-ticket")
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setCpf("");

        response = given()
                .basePath("api/tickets/v1/create-ticket")
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setCustomerMail("");

        response = given()
                .basePath("api/tickets/v1/create-ticket")
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setBrlAmount(null);

        response = given()
                .basePath("api/tickets/v1/create-ticket")
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setUsdAmount(null);

        response = given()
                .basePath("api/tickets/v1/create-ticket")
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void getById() {
        Ticket ticket = mockTicket.mockTicket();

        when(repository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));

        var response = given()
                .basePath("api/tickets/v1/get-ticket/"+ ticket.getTicketId())
                .port(port)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TicketResponseDtoJackson.class);

        assertNotNull(response);
        assertNotNull(response.getLinks().getSelf().getHref());
        assertEquals(response.getTicketId(), ticket.getTicketId());
        assertEquals(response.getEvent().getBairro(),  ticket.getEvent().getBairro());
        assertEquals(response.getEvent().getCidade(),  ticket.getEvent().getCidade());
        assertEquals(response.getEvent().getEventName(),  ticket.getEvent().getEventName());
        assertEquals(response.getEvent().getUf(),  ticket.getEvent().getUf());
        assertEquals(response.getEvent().getId(),  ticket.getEvent().getId());
        assertEquals(response.getEvent().getLogradouro(),  ticket.getEvent().getLogradouro());
        assertNotNull(ticket.getEvent().getDateTime());
        assertEquals(response.getStatus(), ticket.getStatus());
        assertEquals(response.getBRLamount(), ticket.getBRLamount());
        assertEquals(response.getUSDamount(), ticket.getUSDamount());
        assertEquals(response.getCustomerMail(), ticket.getCustomerMail());
        assertEquals(response.getCustomerName(), ticket.getCustomerName());
    }

    @Test
    void getById_withInvalidTicketId_throwsException404() {
        Ticket ticket = mockTicket.mockTicket();

        when(repository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        var response = given()
                .basePath("api/tickets/v1/get-ticket/" + ticket.getTicketId())
                .port(port)
                .when()
                .get()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        assertNotNull(response);
    }

    @Test
    void getByCpf() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(tickets, pageable, tickets.size());

        when(repository.findByCpfAndIsDeletedFalse(anyString(), any(Pageable.class))).thenReturn(page);

        String response = given()
                .basePath("api/tickets/v1/get-ticket-by-cpf/" + tickets.get(0).getCpf())
                .port(port)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        WrapperDto dtos = objectMapper.readValue(response, WrapperDto.class);
        var list = dtos.getListDto().getEventResponseDtoList();

        assertEquals(list.size(), tickets.size());
        assertNotNull(dtos.getLinks().getSelf().getHref());

        for (int i = 0; i < tickets.size(); i ++ ) {
            System.out.println(list.get(i).toString());
            assertEquals(list.get(i).getTicketId(), tickets.get(i).getTicketId());
            assertEquals(list.get(i).getEvent().getBairro(),  tickets.get(i).getEvent().getBairro());
            assertEquals(list.get(i).getEvent().getCidade(),  tickets.get(i).getEvent().getCidade());
            assertEquals(list.get(i).getEvent().getEventName(),  tickets.get(i).getEvent().getEventName());
            assertEquals(list.get(i).getEvent().getUf(),  tickets.get(i).getEvent().getUf());
            assertEquals(list.get(i).getEvent().getId(),  tickets.get(i).getEvent().getId());
            assertEquals(list.get(i).getEvent().getLogradouro(),  tickets.get(i).getEvent().getLogradouro());
            assertNotNull(list.get(i).getEvent().getDateTime());
            assertEquals(list.get(i).getStatus(),  tickets.get(i).getStatus());
            assertEquals(list.get(i).getBRLamount(),  tickets.get(i).getBRLamount());
            assertEquals(list.get(i).getUSDamount(),  tickets.get(i).getUSDamount());
            assertEquals(list.get(i).getCustomerMail(),  tickets.get(i).getCustomerMail());
            assertEquals(list.get(i).getCustomerName(),  tickets.get(i).getCustomerName());
        }
    }

    @Test
    void updateTicket() {
        Ticket old = mockTicket.mockTicket();
        Ticket newTicket = tickets.get(2);
        newTicket.setTicketId(old.getTicketId());
        Event event = new MockEvent().mockEvent();
        when(repository.save(any(Ticket.class))).thenReturn(newTicket);
        when(repository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(old));
        when(eventService.getEvent(anyString())).thenReturn(event);

        TicketRequestDto dto = mockTicket.mockTicketCreateDto();
        dto.setBrlAmount(newTicket.getBRLamount());
        dto.setUsdAmount(newTicket.getUSDamount());
        dto.setCustomerMail(newTicket.getCustomerMail());
        dto.setCustomerName(newTicket.getCustomerName());
        dto.setEventId(newTicket.getEvent().getId());
        dto.setCpf(newTicket.getCpf());


        var response = given()
                .basePath("api/tickets/v1/update-ticket/"+ old.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TicketResponseDtoJackson.class);

        assertNotNull(response);
        assertNotNull(response.getLinks().getSelf().getHref());
        assertEquals(response.getTicketId(), old.getTicketId());
        assertEquals(response.getTicketId(), newTicket.getTicketId());
        assertEquals(response.getEvent().getBairro(),  newTicket.getEvent().getBairro());
        assertEquals(response.getEvent().getCidade(),  newTicket.getEvent().getCidade());
        assertEquals(response.getEvent().getEventName(),  newTicket.getEvent().getEventName());
        assertEquals(response.getEvent().getUf(),  newTicket.getEvent().getUf());
        assertEquals(response.getEvent().getId(),  newTicket.getEvent().getId());
        assertEquals(response.getEvent().getLogradouro(),  newTicket.getEvent().getLogradouro());
        assertNotNull(newTicket.getEvent().getDateTime());
        assertEquals(response.getStatus(), newTicket.getStatus());
        assertEquals(response.getBRLamount(), newTicket.getBRLamount());
        assertEquals(response.getUSDamount(), newTicket.getUSDamount());
        assertEquals(response.getCustomerMail(), newTicket.getCustomerMail());
        assertEquals(response.getCustomerName(), newTicket.getCustomerName());
    }

    @Test
    void updateTicket_withInvalidEventId_throwsException404() {
        Ticket old = mockTicket.mockTicket();

        when(repository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(old));
        when(eventService.getEvent(anyString())).thenThrow(FeignException.NotFound.class);

        var dto = mockTicket.mockTicketCreateDto();

        given()
                .basePath("api/tickets/v1/update-ticket/"+ old.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void updateTicket_withInvalidETicketId_throwsException404() {
        Ticket old = mockTicket.mockTicket();

        when(repository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        var dto = mockTicket.mockTicketCreateDto();

        given()
                .basePath("api/tickets/v1/update-ticket/"+ old.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void udateTicket_withInvalidData_throwsException422() {
        Ticket ticket = mockTicket.mockTicket();
        Event event = new MockEvent().mockEvent();

        TicketRequestDto dto = mockTicket.mockTicketCreateDto();
        dto.setCustomerName("");

        var response = given()
                .basePath("api/tickets/v1/update-ticket/"+ticket.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setEventName("");

        response = given()
                .basePath("api/tickets/v1/update-ticket/"+ticket.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setCpf("");

        response = given()
                .basePath("api/tickets/v1/update-ticket/"+ticket.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setCustomerMail("");

        response = given()
                .basePath("api/tickets/v1/update-ticket/"+ticket.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setBrlAmount(null);

        response = given()
                .basePath("api/tickets/v1/update-ticket/"+ticket.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);

        dto = mockTicket.mockTicketCreateDto();
        dto.setUsdAmount(null);

        response = given()
                .basePath("api/tickets/v1/update-ticket/"+ticket.getTicketId())
                .port(port)
                .contentType("application/json")
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(422)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }

    @Test
    void cancelTicket() {
        Ticket ticket = mockTicket.mockTicket();
        when(repository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(ticket));
        given()
                .basePath("api/tickets/v1/cancel-ticket/"+ticket.getTicketId())
                .port(port)
                .when()
                .delete()
                .then()
                .statusCode(204)
                .extract()
                .body();
    }

    @Test
    void cancelTicket_withInvalidTicketId_throwsException404() {
        when(repository.findByTicketIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());
        given()
                .basePath("api/tickets/v1/cancel-ticket/"+ "invalid")
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
    void checkTicketsByEventId() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(tickets, pageable, tickets.size());

        when(repository.findAllByEvent_IdAndIsDeletedFalse(anyString(), any(Pageable.class))).thenReturn(page);

        var response = given()
                .basePath("api/tickets/v1/check-tickets-by-event/" + tickets.get(0).getEvent().getId())
                .port(port)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperDto dtos = objectMapper.readValue(response, WrapperDto.class);
        var list = dtos.getListDto().getEventResponseDtoList();

        assertEquals(list.size(), tickets.size());
        assertNotNull(dtos.getLinks().getSelf().getHref());

        for (int i = 0; i < tickets.size(); i ++ ) {
            assertEquals(list.get(i).getTicketId(), tickets.get(i).getTicketId());
            assertEquals(list.get(i).getEvent().getBairro(),  tickets.get(i).getEvent().getBairro());
            assertEquals(list.get(i).getEvent().getCidade(),  tickets.get(i).getEvent().getCidade());
            assertEquals(list.get(i).getEvent().getEventName(),  tickets.get(i).getEvent().getEventName());
            assertEquals(list.get(i).getEvent().getUf(),  tickets.get(i).getEvent().getUf());
            assertEquals(list.get(i).getEvent().getId(),  tickets.get(i).getEvent().getId());
            assertEquals(list.get(i).getEvent().getLogradouro(),  tickets.get(i).getEvent().getLogradouro());
            assertNotNull(list.get(i).getEvent().getDateTime());
            assertEquals(list.get(i).getStatus(),  tickets.get(i).getStatus());
            assertEquals(list.get(i).getBRLamount(),  tickets.get(i).getBRLamount());
            assertEquals(list.get(i).getUSDamount(),  tickets.get(i).getUSDamount());
            assertEquals(list.get(i).getCustomerMail(),  tickets.get(i).getCustomerMail());
            assertEquals(list.get(i).getCustomerName(),  tickets.get(i).getCustomerName());
        }
    }

    @Test
    void testError500() {
        when(repository.findByTicketIdAndIsDeletedFalse(anyString())).thenThrow(new RuntimeException());
        var response = given()
                .basePath("api/tickets/v1/get-ticket/" + "invalid")
                .port(port)
                .when()
                .get()
                .then()
                .statusCode(500)
                .extract()
                .body()
                .as(ExceptionResponse.class);
    }
}
