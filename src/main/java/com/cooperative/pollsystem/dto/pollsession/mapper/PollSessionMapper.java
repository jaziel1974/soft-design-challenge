package com.cooperative.pollsystem.dto.pollsession.mapper;

import com.cooperative.pollsystem.dto.agenda.mapper.AgendaMapper;
import com.cooperative.pollsystem.dto.pollsession.PollSessionResponse;
import com.cooperative.pollsystem.model.PollSession;
import com.cooperative.pollsystem.model.enums.PollSessionStatus;

import java.time.LocalDateTime;

public class PollSessionMapper {
    public static PollSessionResponse toResponse(PollSession pollSession) {
        PollSessionStatus status;
        if (pollSession.getValidUntil().isAfter(LocalDateTime.now())) {
            status = PollSessionStatus.OPENED;
        } else {
            status = PollSessionStatus.CLOSED;
        }

        return new PollSessionResponse(
                pollSession.getId(),
                status,
                AgendaMapper.toResponse(pollSession.getAgenda()),
                pollSession.getPollDuration(),
                pollSession.getVotesYes(),
                pollSession.getVotesNo());
    }
}