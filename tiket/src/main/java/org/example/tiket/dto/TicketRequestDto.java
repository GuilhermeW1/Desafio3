package org.example.tiket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.lang.Double;
import java.util.Objects;

public class TicketRequestDto {
    @CPF
    private String cpf;
    @NotBlank
    private String customerName;
    @NotBlank
    private String customerMail;
    @NotBlank
    private String eventId;
    @NotBlank
    private String eventName;
    @NotNull
    private Double brlAmount;
    @NotNull
    private Double usdAmount;

    public TicketRequestDto() {
    }

    public TicketRequestDto(String cpf, String customerName, String customerMail, String eventId, String eventName, Double BRLamount, Double USDamount) {
        this.cpf = cpf;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.eventId = eventId;
        this.eventName = eventName;
        this.brlAmount = BRLamount;
        this.usdAmount = USDamount;
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

    public Double getBrlAmount() {
        return brlAmount;
    }

    public void setBrlAmount(Double brlAmount) {
        this.brlAmount = brlAmount;
    }

    public Double getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(Double usdAmount) {
        this.usdAmount = usdAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketRequestDto that = (TicketRequestDto) o;
        return Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cpf);
    }
}
