package org.example.events.moks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MockListDto {
    @JsonProperty("eventResponseDtoList")
    private List<EventDtoJackson> eventResponseDtoList;

    public MockListDto() {}

    public List<EventDtoJackson> getEventResponseDtoList() {
        return eventResponseDtoList;
    }

    public void setEventResponseDtoList(List<EventDtoJackson> eventResponseDtoList) {
        this.eventResponseDtoList = eventResponseDtoList;
    }
}
