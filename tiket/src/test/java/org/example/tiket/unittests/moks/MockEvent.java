package org.example.tiket.unittests.moks;

import org.example.tiket.entity.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockEvent {

    private static final String DEFAULT_ID = "uuid";
    private static final String DEFAULT_EVENT_NAME = "mockEvent";
    private static final LocalDateTime DEFAULT_DATE_TIME = LocalDateTime.now();
    private static final String DEFAULT_CEP = "12345";
    private static final String DEFAULT_CIDADE = "Cidade";
    private static final String DEFAULT_BAIRRO = "Bairro";
    private static final String DEFAULT_LOGRADOURO = "Logradouro";
    private static final String DEFAULT_UF = "Rs";
    private static final boolean DEFAULT_DELETED = false;
    private static final LocalDateTime DEFAULT_CREATED_AT = LocalDateTime.now();
    private static final LocalDateTime DEFAULT_UPDATED_AT = LocalDateTime.now();

    public Event mockEvent() {
        Event e = new Event();
        e.setId(DEFAULT_ID);
        e.setEventName(DEFAULT_EVENT_NAME);
        e.setDateTime(DEFAULT_DATE_TIME);
        e.setCidade(DEFAULT_CIDADE);
        e.setBairro(DEFAULT_BAIRRO);
        e.setLogradouro(DEFAULT_LOGRADOURO);
        e.setUf(DEFAULT_UF);
        return e;
    }
//
//    public EventRequestDto mockEventCreateDto() {
//        EventRequestDto dto = new EventRequestDto();
//        dto.setEventName(DEFAULT_EVENT_NAME);
//        dto.setDateTime(DEFAULT_DATE_TIME);
//        dto.setCep(DEFAULT_CEP);
//        return dto;
//    }
//
//    public EventResponseDto mockEventResponseDto() {
//        EventResponseDto dto = new EventResponseDto();
//        dto.setId(DEFAULT_ID);
//        dto.setEventName(DEFAULT_EVENT_NAME);
//        dto.setDateTime(DEFAULT_DATE_TIME);
//        dto.setCep(DEFAULT_CEP);
//        dto.setCidade(DEFAULT_CIDADE);
//        dto.setBairro(DEFAULT_BAIRRO);
//        dto.setLogradouro(DEFAULT_LOGRADOURO);
//        dto.setUf(DEFAULT_UF);
//
//        return dto;
//    }
//
//    public List<EventResponseDto> mockEventResponseDtoList() {
//        List<EventResponseDto> dtos = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            EventResponseDto dto = new EventResponseDto();
//            dto.setId(DEFAULT_ID + i);
//            dto.setEventName(DEFAULT_EVENT_NAME + i);
//            dto.setDateTime(DEFAULT_DATE_TIME);
//            dto.setCep(DEFAULT_CEP + i);
//            dto.setCidade(DEFAULT_CIDADE + i);
//            dto.setBairro(DEFAULT_BAIRRO + i);
//            dto.setLogradouro(DEFAULT_LOGRADOURO + i);
//            dto.setUf(DEFAULT_UF + i);
//            dtos.add(dto);
//        }
//        return dtos;
//    }
//
//    public List<Event> mockEventList() {
//        List<Event> events = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Event e = new Event();
//            e.setId(DEFAULT_ID);
//            e.setEventName(DEFAULT_EVENT_NAME + i);
//            e.setDateTime(DEFAULT_DATE_TIME);
//            e.setCep(DEFAULT_CEP +i);
//            e.setCidade(DEFAULT_CIDADE +i);
//            e.setBairro(DEFAULT_BAIRRO + i);
//            e.setLogradouro(DEFAULT_LOGRADOURO +i);
//            e.setUf(DEFAULT_UF +i);
//            e.setDeleted(DEFAULT_DELETED);
//            e.setCreatedAt(DEFAULT_CREATED_AT);
//            e.setUpdatedAt(DEFAULT_UPDATED_AT);
//            e.setDeletedAt(null);
//
//            events.add(e);
//        }
//        return events;
//    }
}
