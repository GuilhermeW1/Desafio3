package org.example.tiket.moks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperDto {
    @JsonProperty("_links")
    private MockLink links;

    @JsonProperty("_embedded")
    private MockListDto listDto;

    public WrapperDto() {}

    public MockListDto getListDto() {
        return listDto;
    }

    public void setListDto(MockListDto listDto) {
        this.listDto = listDto;
    }

    public MockLink getLinks() {
        return links;
    }

    public void setLinks(MockLink links) {
        this.links = links;
    }
}
