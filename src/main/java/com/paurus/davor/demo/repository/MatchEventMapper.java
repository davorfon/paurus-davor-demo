package com.paurus.davor.demo.repository;

import com.paurus.davor.demo.model.MatchEventMinMaxDate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchEventMapper {

    MatchEventMinMaxDate findMatchEventMinMaxDateInsert();
}
