package com.paurus.davor.demo.controller;

import com.paurus.davor.demo.dto.BettingRequest;
import com.paurus.davor.demo.dto.BettingResponse;
import com.paurus.davor.demo.service.BettingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/betting")
public class BettingController {

    private static final Logger logger = LogManager.getLogger(BettingController.class);

    @Autowired
    private BettingService bettingService;

    @PostMapping("/bet")
    public ResponseEntity<BettingResponse> bet(@RequestBody BettingRequest request) {
        try {
            return ResponseEntity.ok(bettingService.bet(request));
        } catch (Exception e) {
            logger.error("bet", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
