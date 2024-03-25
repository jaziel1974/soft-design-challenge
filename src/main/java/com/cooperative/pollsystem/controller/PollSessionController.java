package com.cooperative.pollsystem.controller;

import com.cooperative.pollsystem.dto.pollsession.PollSessionRequest;
import com.cooperative.pollsystem.dto.pollsession.PollSessionResponse;
import com.cooperative.pollsystem.dto.pollsession.mapper.PollSessionMapper;
import com.cooperative.pollsystem.model.PollSession;
import com.cooperative.pollsystem.model.exceptions.BusinessException;
import com.cooperative.pollsystem.service.PollSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pollSession")
public class PollSessionController {
    Logger logger = LoggerFactory.getLogger(PollSessionController.class);

    private final PollSessionService pollSessionService;

    public PollSessionController(PollSessionService pollSessionService) {
        this.pollSessionService = pollSessionService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody PollSessionRequest pollSessionRequest) {
        try {
        PollSession pollSession = new PollSession();
        pollSession.setId(UUID.randomUUID().toString());
        pollSession.setPollDuration(pollSessionRequest.pollDuration());
        pollSessionService.populateAgenda(pollSession, pollSessionRequest.agendaId());
        PollSessionResponse createdPollSession = PollSessionMapper.toResponse(pollSessionService.create(pollSession));
            return new ResponseEntity<>(createdPollSession, null, 200);
        }
        catch (BusinessException e){
            logger.error("Error creating poll session", e);
            return new ResponseEntity<>(e.getMessage(), null, 400);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        try {
            PollSession pollSession = pollSessionService.findById(id);
            PollSessionResponse response = PollSessionMapper.toResponse(pollSession);
            return new ResponseEntity<>(response, null, 200);
        }
        catch (BusinessException e){
            logger.error("Error finding poll session", e);
            return new ResponseEntity<>(e.getMessage(), null, 404);
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> findByAgendaId(@RequestParam("agendaId") String agendaId) {
        try {
            PollSession pollSession = pollSessionService.findByAgendaId(agendaId);
            PollSessionResponse response = PollSessionMapper.toResponse(pollSession);
            return new ResponseEntity<>(response, null, 200);
        }
        catch (BusinessException e){
            logger.error("Error finding poll session by agenda", e);
            return new ResponseEntity<>(e.getMessage(), null, 404);
        }
    }

}