package org.example.tiket.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class TicketRequestDto {
    private String ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private String eventId;
    private String eventName;
    private BigDecimal BRLamount;
    private BigDecimal USDamount;

    public TicketRequestDto() {}

    public TicketRequestDto(String ticketId,String cpf, String customerName, String customerMail, String eventId, String eventName, BigDecimal BRLamount, BigDecimal USDamount) {
        this.ticketId = ticketId;
        this.cpf = cpf;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.eventId = eventId;
        this.eventName = eventName;
        this.BRLamount = BRLamount;
        this.USDamount = USDamount;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMail() {
        return customerMail;
    }

    public void setCustomerMail(String customerMail) {
        this.customerMail = customerMail;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public BigDecimal getBRLamount() {
        return BRLamount;
    }

    public void setBRLamount(BigDecimal BRLamount) {
        this.BRLamount = BRLamount;
    }

    public BigDecimal getUSDamount() {
        return USDamount;
    }

    public void setUSDamount(BigDecimal USDamount) {
        this.USDamount = USDamount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketRequestDto that = (TicketRequestDto) o;
        return Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ticketId);
    }
}
