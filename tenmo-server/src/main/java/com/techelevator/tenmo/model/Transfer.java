package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Transfer {


    private Long senderId;
    private Long transferId;
    private Long recipientId;
    private Timestamp timestamp;
    private String transferStatus;
    private BigDecimal transferAmount;

    public Transfer () { }
    Account account = new Account();

    public Transfer(Long transferId, Long senderId, Long recipientId, Timestamp timestamp, String transferStatus, BigDecimal transferAmount){
        this.transferId = transferId;
        this.senderId = account.getId();
        this.recipientId = account.getId();
        this.timestamp = timestamp;
        this.transferStatus = transferStatus;
        this.transferAmount = transferAmount;
    }

    public Transfer(Long senderId, Long recipientId, BigDecimal transferAmount){
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.transferAmount = transferAmount;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Long getTransferId() {
        return transferId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
}
