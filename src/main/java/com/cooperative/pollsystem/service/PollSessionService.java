package com.cooperative.pollsystem.service;

import com.cooperative.pollsystem.model.Agenda;
import com.cooperative.pollsystem.model.exceptions.DuplicatedIdException;
import com.cooperative.pollsystem.model.exceptions.GenericErrorException;
import com.cooperative.pollsystem.model.PollSession;
import com.cooperative.pollsystem.repository.AgendaRepository;
import com.cooperative.pollsystem.repository.PollSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

public class PollSessionService {
    Logger logger = LoggerFactory.getLogger(PollSessionService.class);

    private final AgendaRepository agendaRepository;
    private final PollSessionRepository pollSessionRepository;

    public PollSessionService(AgendaRepository agendaRepository, PollSessionRepository pollSessionRepository) {
        this.agendaRepository = agendaRepository;
        this.pollSessionRepository = pollSessionRepository;
    }

    public PollSession create(PollSession pollSession) {
        if (pollSession.getAgenda() == null) {
            logger.error("Agenda is required");
            throw new GenericErrorException("Agenda is required");
        }

        PollSession lastPoll = pollSessionRepository.findByAgendaId(pollSession.getAgenda().getId());

        if (lastPoll != null && lastPoll.getValidUntil().isAfter(LocalDateTime.now())) {
            logger.error("There is already a poll session opened for this agenda");
            throw new DuplicatedIdException("There is already a poll session opened for this agenda");
        }

        if (pollSession.getPollDuration() != null && pollSession.getPollDuration() < 1 && (pollSession.getAgenda().getPollDuration() == null)) {
            logger.error("Poll duration must be a number greater than 0");
            throw new GenericErrorException("Poll duration must be a number greater than 0");
        }

        if (pollSession.getPollDuration() == null) {
            pollSession.setPollDuration(pollSession.getAgenda().getPollDuration());
        }
        pollSession.setValidUntil(LocalDateTime.now().plusMinutes(pollSession.getPollDuration()));

        return pollSessionRepository.save(pollSession);
    }

    public PollSession findById(String id) {
        return pollSessionRepository.findById(id).orElseThrow(() -> {
            logger.error(POLLSESSION_NOT_FOUND);
            return new GenericErrorException(POLLSESSION_NOT_FOUND);
        });
    }

    public PollSession findByAgendaId(String id) {
        PollSession pollSession = pollSessionRepository.findByAgendaId(id);
        if (pollSession == null) {
            logger.error(POLLSESSION_NOT_FOUND);
            throw new GenericErrorException(POLLSESSION_NOT_FOUND);
        }

        return pollSession;
    }

    public void populateAgenda(PollSession pollSession, String agendaId) {
        Optional<Agenda> agendaOptional = agendaRepository.findById(agendaId);
        agendaOptional.orElseThrow(() -> {
            logger.error("Agenda not found");
            return new GenericErrorException("Agenda not found");
        });
        pollSession.setAgenda(agendaOptional.get());
    }

    private static final String POLLSESSION_NOT_FOUND = "Poll session not found";
}