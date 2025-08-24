package com.paurus.davor.demo.service;

import com.paurus.davor.demo.dto.BettingRequest;
import com.paurus.davor.demo.dto.BettingResponse;
import com.paurus.davor.demo.model.TaxRule;
import com.paurus.davor.demo.model.enums.TaxType;
import com.paurus.davor.demo.repository.TaxRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BettingService {

    @Autowired
    private TaxRuleRepository taxRuleRepo;

    @Autowired
    private PricingService pricingService;

    public BettingResponse bet(BettingRequest req) throws Exception {

        if (req.getTraderId() == null) {
            throw new IllegalArgumentException("Bad request, missing param 'traderId'");
        }

        TaxRule rule = taxRuleRepo.findByTraderId(req.getTraderId());
        if (rule == null) {
            throw new IllegalArgumentException("No tax rule for trader " + req.getTraderId());
        }

        BigDecimal played = req.getPlayedAmount();
        BigDecimal odd = req.getOdd();

        BigDecimal before = played
                .multiply(odd)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal amount;
        if (rule.getTaxType() == TaxType.General) {
            amount = before;
        } else {
            // TaxType.Winnings
            amount = before.subtract(played);
        }

        BigDecimal taxAmount = pricingService.calculateTaxAmount(amount, rule);
        String taxRateDisplay = pricingService.getTaxRateDisplayValue(rule);

        BigDecimal afterTax = pricingService.calculateNetAfterTax(before, taxAmount);

        return new BettingResponse(before, before, afterTax, taxRateDisplay, taxAmount);
    }
}
