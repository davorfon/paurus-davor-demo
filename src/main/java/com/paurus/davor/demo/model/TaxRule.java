package com.paurus.davor.demo.model;


import com.paurus.davor.demo.model.enums.TaxMode;
import com.paurus.davor.demo.model.enums.TaxType;

import java.math.BigDecimal;

public class TaxRule {

    private Long traderId;
    private TaxType taxType;
    private TaxMode taxMode;
    private BigDecimal value;

    public TaxRule() {

    }

    public Long getTraderId() {
        return traderId;
    }

    public void setTraderId(Long traderId) {
        this.traderId = traderId;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public TaxMode getTaxMode() {
        return taxMode;
    }

    public void setTaxMode(TaxMode taxMode) {
        this.taxMode = taxMode;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TaxRule{" +
                "traderId=" + traderId +
                ", taxType=" + taxType +
                ", taxMode=" + taxMode +
                ", value=" + value +
                '}';
    }
}
