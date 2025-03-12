package org.example.events.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class EventRequestDto implements Serializable {
    @NotBlank
    @Size(min = 3, max = 100)
    private String eventName;
    @NotNull
    private LocalDateTime dateTime;
    @NotBlank
    private String cep;

    public EventRequestDto() {}


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
        EventRequestDto that = (EventRequestDto) o;
        return Objects.equals(eventName, that.eventName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventName);
    }
}
