package com.cooperative.pollsystem.dto.pollsession;

public record PollSessionRequest(
        String agendaId,
        Integer pollDuration) {
}
