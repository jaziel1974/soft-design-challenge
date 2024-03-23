package com.cooperative.pollsystem.service;

import com.cooperative.pollsystem.model.Agenda;
import com.cooperative.pollsystem.model.exceptions.DuplicatedIdException;
import com.cooperative.pollsystem.model.exceptions.GenericErrorException;
import com.cooperative.pollsystem.model.PollSession;
import com.cooperative.pollsystem.repository.AgendaRepository;
import com.cooperative.pollsystem.repository.PollSessionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class PollSessionService {
    private final AgendaRepository agendaRepository;
    private final PollSessionRepository pollSessionRepository;

    public PollSessionService(AgendaRepository agendaRepository, PollSessionRepository pollSessionRepository) {
        this.agendaRepository = agendaRepository;
        this.pollSessionRepository = pollSessionRepository;
    }

    public PollSession create(PollSession pollSession) {
        if(pollSession.getAgenda() == null)
            throw new GenericErrorException("Agenda is required");

        PollSession lastPoll = pollSessionRepository.findByAgendaId(pollSession.getAgenda().getId());

        if(lastPoll != null && lastPoll.getValidUntil().isAfter(LocalDateTime.now()))
            throw new DuplicatedIdException("There is already a poll session opened for this agenda");

        if(pollSession.getPollDuration() != null && pollSession.getPollDuration() < 1 && (pollSession.getAgenda().getPollDuration() == null)){
                throw new GenericErrorException("Poll duration must be a number greater than 0");
        }

        if(pollSession.getPollDuration() == null){
            pollSession.setPollDuration(1);
        }
        pollSession.setValidUntil(LocalDateTime.now().plusMinutes(pollSession.getPollDuration()));

        return pollSessionRepository.save(pollSession);
    }

    public PollSession findById(String id) {
        return pollSessionRepository.findById(id).orElseThrow(() -> new GenericErrorException("Poll session not found"));
    }

    public void populateAgenda(PollSession pollSession, String agendaId) {
        Optional<Agenda> agendaOptional = agendaRepository.findById(agendaId);
        agendaOptional.orElseThrow(() -> new GenericErrorException("Agenda not found"));
        pollSession.setAgenda(agendaOptional.get());
    }
}