package org.example.events.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class EventCreateDto implements Serializable {
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;

    public EventCreateDto() {}

    public EventCreateDto(String eventName, LocalDateTime dateTime, String cep) {
        this.eventName = eventName;
        this.dateTime = dateTime;
        this.cep = cep;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EventCreateDto that = (EventCreateDto) o;
        return Objects.equals(eventName, that.eventName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventName);
    }
}
