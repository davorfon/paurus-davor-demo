package com.paurus.davor.demo.service;

import com.paurus.davor.demo.model.TaxRule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingService {

    public BigDecimal calculateTaxAmount(BigDecimal amount, TaxRule rule) {

        if (amount == null || rule == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }

        switch (rule.getTaxMode()) {
            case Rate:
                return amount.multiply(rule.getValue())
                        .setScale(2, RoundingMode.HALF_UP);

            case Amount:
                return rule.getValue()
                        .setScale(2, RoundingMode.HALF_UP);

            default:
                throw new UnsupportedOperationException("Unsupported tax mode: " + rule.getTaxMode());
        }
    }

    public String getTaxRateDisplayValue(TaxRule rule) {

        switch (rule.getTaxMode()) {
            case Rate:
                return rule.getValue().movePointRight(2).setScale(2, RoundingMode.HALF_UP) + "%";

            case Amount:
                return rule.getValue().setScale(2, RoundingMode.HALF_UP) + " EUR";

            default:
                throw new UnsupportedOperationException("Unsupported tax mode: " + rule.getTaxMode());
        }
    }

    public BigDecimal calculateNetAfterTax(BigDecimal before, BigDecimal taxAmount) throws Exception {
        if (before == null || taxAmount == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }

        BigDecimal after = before.subtract(taxAmount);
        if (after.signum() < 0) {
            throw new Exception("Net amount is negative");
        }

        return after.setScale(2, RoundingMode.HALF_UP);
    }
}
