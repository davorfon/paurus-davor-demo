package com.paurus.davor.demo.dto;


import java.math.BigDecimal;

public class BettingResponse {

    private BigDecimal possibleReturnAmount;
    private BigDecimal possibleReturnAmountBefTax;
    private BigDecimal possibleReturnAmountAfterTax;
    private String taxRate;
    private BigDecimal taxAmount;

    public BettingResponse() {
    }

    public BettingResponse(BigDecimal possibleReturnAmount, BigDecimal possibleReturnAmountBefTax, BigDecimal possibleReturnAmountAfterTax, String taxRate, BigDecimal taxAmount) {
        this.possibleReturnAmount = possibleReturnAmount;
        this.possibleReturnAmountBefTax = possibleReturnAmountBefTax;
        this.possibleReturnAmountAfterTax = possibleReturnAmountAfterTax;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
    }

    public BigDecimal getPossibleReturnAmount() {
        return possibleReturnAmount;
    }

    public void setPossibleReturnAmount(BigDecimal possibleReturnAmount) {
        this.possibleReturnAmount = possibleReturnAmount;
    }

    public BigDecimal getPossibleReturnAmountBefTax() {
        return possibleReturnAmountBefTax;
    }

    public void setPossibleReturnAmountBefTax(BigDecimal possibleReturnAmountBefTax) {
        this.possibleReturnAmountBefTax = possibleReturnAmountBefTax;
    }

    public BigDecimal getPossibleReturnAmountAfterTax() {
        return possibleReturnAmountAfterTax;
    }

    public void setPossibleReturnAmountAfterTax(BigDecimal possibleReturnAmountAfterTax) {
        this.possibleReturnAmountAfterTax = possibleReturnAmountAfterTax;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Override
    public String toString() {
        return "BetResponse{" +
                "possibleReturnAmount=" + possibleReturnAmount +
                ", possibleReturnAmountBefTax=" + possibleReturnAmountBefTax +
                ", possibleReturnAmountAfterTax=" + possibleReturnAmountAfterTax +
                ", taxRate='" + taxRate + '\'' +
                ", taxAmount=" + taxAmount +
                '}';
    }
}
