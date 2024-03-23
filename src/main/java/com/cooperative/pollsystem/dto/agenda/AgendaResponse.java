package com.cooperative.pollsystem.dto.agenda;

import java.time.LocalDateTime;

public record AgendaResponse(String id, String title, Integer pollDuration, LocalDateTime pollDate){
}
