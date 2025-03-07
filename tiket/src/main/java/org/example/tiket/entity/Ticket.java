package org.example.tiket.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {
   private String ticketId;
   private String cpf;
   private String customerName;
   private String customerMail;
   private Event event;
   private BigDecimal BRLamount;
   private BigDecimal USDamount;
   private String status;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private LocalDateTime deletedAt;
   private Boolean isDeleted;

   public Ticket() {}

    public Ticket(String ticketId, String cpf, String customerName, String customerMail, Event event, BigDecimal BRLamount, BigDecimal USDamount, String status, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Boolean isDeleted) {
        this.ticketId = ticketId;
        this.cpf = cpf;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.event = event;
        this.BRLamount = BRLamount;
        this.USDamount = USDamount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ticketId);
    }
}
