package org.example.tickets.dto;

import org.example.tickets.entity.Event;

import java.io.Serializable;
import java.lang.Double;
import java.util.Objects;
import org.springframework.hateoas.RepresentationModel;

public class TicketResponseDto extends RepresentationModel<TicketResponseDto> implements Serializable {
    private String ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private Event event;
    private Double BRLamount;
    private Double USDamount;
    private String status;

    public TicketResponseDto() {}


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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketResponseDto that = (TicketResponseDto) o;
        return Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ticketId);
    }
}
