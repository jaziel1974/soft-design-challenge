package com.cooperative.pollsystem.controller;

import com.cooperative.pollsystem.dto.agenda.AgendaRequest;
import com.cooperative.pollsystem.dto.agenda.AgendaResponse;
import com.cooperative.pollsystem.dto.agenda.mapper.AgendaMapper;
import com.cooperative.pollsystem.model.Agenda;
import com.cooperative.pollsystem.model.exceptions.BusinessException;
import com.cooperative.pollsystem.service.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agenda")
public class AgendaController {
    Logger logger = LoggerFactory.getLogger(AgendaController.class);

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping("/{id}")
    public AgendaResponse findById(@PathVariable String id) {
        Agenda agenda = agendaService.findById(id);
        return AgendaMapper.toResponse(agenda);
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody AgendaRequest agendaRequest) {
        Agenda agenda = AgendaMapper.toDomain(agendaRequest);
        try {
            var createdAgenda = agendaService.create(agenda);
            var createdAgendaResponse = AgendaMapper.toResponse(createdAgenda);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAgendaResponse);
        } catch (BusinessException e) {
            logger.error("Error creating agenda", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Object> delete(@PathVariable("title") String title) {
        agendaService.delete(title);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
