package com.paurus.davor.demo.repository;

import com.paurus.davor.demo.model.TaxRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TaxRuleRepository {

    @Autowired
    private TaxRuleMapper taxRuleMapper;

    public TaxRule findByTraderId(Long traderId) {
        return taxRuleMapper.findByTraderId(traderId);
    }
}
