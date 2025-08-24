package com.paurus.davor.demo.repository;

import com.paurus.davor.demo.model.MatchEventMinMaxDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MatchEventRepository {

    @Autowired
    private MatchEventMapper matchEventMapper;

    public MatchEventMinMaxDate findMatchEventMinMaxDateInsert() {
        return matchEventMapper.findMatchEventMinMaxDateInsert();
    }
}
