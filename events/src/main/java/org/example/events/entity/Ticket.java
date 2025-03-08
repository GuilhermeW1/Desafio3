package org.example.events.entity;

import java.util.Objects;

public class Ticket {

    private String ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private Event event;
    private Double BRLamount;
    private Double USDamount;
    private String status;

    public Ticket() {}

    public Ticket(String ticketId, String customerName, String customerMail, Event event, Double BRLamount, Double USDamount, String status) {
        this.ticketId = ticketId;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.event = event;
        this.BRLamount = BRLamount;
        this.USDamount = USDamount;
        this.status = status;
    }

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
        Ticket that = (Ticket) o;
        return Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ticketId);
    }
}
