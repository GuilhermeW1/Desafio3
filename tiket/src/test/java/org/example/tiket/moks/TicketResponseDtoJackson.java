package org.example.tiket.moks;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.tiket.entity.Event;

public class TicketResponseDtoJackson {

    private String ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private EventMockDtoJackson event;
    private Double BRLamount;
    private Double USDamount;
    private String status;
    @JsonProperty("_links")
    private MockLink links;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public MockLink getLinks() {
        return links;
    }

    public void setLinks(MockLink links) {
        this.links = links;
    }

    public TicketResponseDtoJackson() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getUSDamount() {
        return USDamount;
    }

    public void setUSDamount(Double USDamount) {
        this.USDamount = USDamount;
    }

    public Double getBRLamount() {
        return BRLamount;
    }

    public void setBRLamount(Double BRLamount) {
        this.BRLamount = BRLamount;
    }

    public EventMockDtoJackson getEvent() {
        return event;
    }

    public void setEvent(EventMockDtoJackson event) {
        this.event = event;
    }

    public String getCustomerMail() {
        return customerMail;
    }

    public void setCustomerMail(String customerMail) {
        this.customerMail = customerMail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

}
