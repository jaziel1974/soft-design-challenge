package com.cooperative.pollsystem.service;

import com.cooperative.pollsystem.model.Agenda;
import com.cooperative.pollsystem.model.exceptions.DuplicatedIdException;
import com.cooperative.pollsystem.model.exceptions.GenericErrorException;
import com.cooperative.pollsystem.repository.AgendaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

public class AgendaService {
    Logger logger = LoggerFactory.getLogger(AgendaService.class);
    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public Agenda create(Agenda agenda) {
        UUID id = UUID.randomUUID();

        if (agenda.getPollDate().isBefore(LocalDateTime.now())) {
            logger.error("Poll date cannot be lesser than today");
            throw new GenericErrorException("Poll date cannot be lesser than today");
        }
        if (agenda.getPollDuration() != null && agenda.getPollDuration() < 1) {
            logger.error("Poll must last one minute at least");
            throw new GenericErrorException("Poll must last one minute at least");
        }

        var existentAgenda = agendaRepository.findByTitle(agenda.getTitle());
        if (existentAgenda != null){
            logger.error("Agenda with title " + agenda.getTitle() + " already exists");
            throw new DuplicatedIdException("Agenda with title " + agenda.getTitle() + " already exists");
        }

        agenda.setId(id.toString());
        return agendaRepository.save(agenda);
    }

    public void delete(String title) {
        agendaRepository.delete(title);
    }

    public Agenda findById(String id) {
        return agendaRepository.findById(id).orElseThrow(() -> {
            logger.error("Agenda not found");
            return new GenericErrorException("Agenda not found");
        });
    }
}
