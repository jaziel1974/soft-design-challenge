package com.cooperative.pollsystem.dto.pollsession;

import com.cooperative.pollsystem.dto.agenda.AgendaResponse;
import com.cooperative.pollsystem.model.enums.PollSessionStatus;

public record PollSessionResponse(
        String id,
        PollSessionStatus status,
        AgendaResponse agenda,
        Integer pollDuration,
        Integer votesYes,
        Integer votesNo){
}