package com.paurus.davor.demo.dto;

import java.math.BigDecimal;

public class BettingRequest {

    private Long traderId;
    private BigDecimal playedAmount;
    private BigDecimal odd;

    public Long getTraderId() {
        return traderId;
    }

    public void setTraderId(Long traderId) {
        this.traderId = traderId;
    }

    public BigDecimal getPlayedAmount() {
        return playedAmount;
    }

    public void setPlayedAmount(BigDecimal playedAmount) {
        this.playedAmount = playedAmount;
    }

    public BigDecimal getOdd() {
        return odd;
    }

    public void setOdd(BigDecimal odd) {
        this.odd = odd;
    }

    @Override
    public String toString() {
        return "BetRequest{" +
                "traderId=" + traderId +
                ", playedAmount=" + playedAmount +
                ", odd=" + odd +
                '}';
    }
}
