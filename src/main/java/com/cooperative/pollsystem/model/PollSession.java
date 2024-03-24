package com.cooperative.pollsystem.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "pollSession")
@Getter
@Setter
public class PollSession{

    @Id
    private String id;
    private Agenda agenda;
    private Integer pollDuration;
    private Integer votesYes;
    private Integer votesNo;
    @CreatedDate private LocalDateTime createdAt;
    private LocalDateTime validUntil;
}
