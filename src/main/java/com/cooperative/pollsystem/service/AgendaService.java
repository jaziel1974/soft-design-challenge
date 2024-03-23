package com.cooperative.pollsystem.service;

import com.cooperative.pollsystem.model.Agenda;
import com.cooperative.pollsystem.model.exceptions.DuplicatedIdException;
import com.cooperative.pollsystem.model.exceptions.GenericErrorException;
import com.cooperative.pollsystem.repository.AgendaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class AgendaService {
    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public Agenda create(Agenda agenda) {
        UUID id = UUID.randomUUID();

        if(agenda.getPollDate().isBefore(LocalDateTime.now()))
            throw new GenericErrorException("Poll date cannot be lesser than today");
        if(agenda.getPollDuration() != null && agenda.getPollDuration() < 1)
            throw new GenericErrorException("Poll must last one minute at least");

        var existentAgenda = agendaRepository.findByTitle(agenda.getTitle());
        if (existentAgenda != null)
            throw  new DuplicatedIdException("Agenda with title " + agenda.getTitle() + " already exists");

        agenda.setId(id.toString());
        return agendaRepository.save(agenda);
    }

    public void delete(String title) {
        agendaRepository.delete(title);
    }

    public Agenda findById(String id) {
        return agendaRepository.findById(id).orElseThrow(() -> new GenericErrorException("Agenda not found"));
    }
}
