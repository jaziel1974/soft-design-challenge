package com.cooperative.pollsystem.dto.agenda.mapper;

import com.cooperative.pollsystem.dto.agenda.AgendaRequest;
import com.cooperative.pollsystem.dto.agenda.AgendaResponse;
import com.cooperative.pollsystem.model.Agenda;

public interface AgendaMapper {
    public static Agenda toDomain(AgendaRequest agendaRequest) {
        return new Agenda(agendaRequest.title(), agendaRequest.pollDuration(), agendaRequest.pollDate());
    }

    public static AgendaResponse toResponse(Agenda agenda) {
        return new AgendaResponse(agenda.getId(), agenda.getTitle(), agenda.getPollDuration(), agenda.getPollDate());
    }
}