package org.example.tiket.moks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MockListDto {
    @JsonProperty("ticketResponseDtoList")
    private List<TicketResponseDtoJackson> eventResponseDtoList;

    public MockListDto() {}

    public List<TicketResponseDtoJackson> getEventResponseDtoList() {
        return eventResponseDtoList;
    }

    public void setEventResponseDtoList(List<TicketResponseDtoJackson> eventResponseDtoList) {
        this.eventResponseDtoList = eventResponseDtoList;
    }
}
