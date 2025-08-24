package com.paurus.davor.demo.controller;

import com.paurus.davor.demo.service.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private static final Logger logger = LogManager.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @PostMapping("/processEvents")
    public ResponseEntity<String> processEvents() {
        try {
            eventService.processEvents();
            return ResponseEntity.ok("Success");

        } catch (Exception e) {
            logger.error("processEvents", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }
}
