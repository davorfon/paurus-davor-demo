package com.paurus.davor.demo.repository;

import com.paurus.davor.demo.model.TaxRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaxRuleMapper {
    TaxRule findByTraderId(@Param("traderId") Long traderId);

}
