package com.cooperative.pollsystem.dto.agenda;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AgendaRequest(String title, Integer pollDuration, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime pollDate){
}
