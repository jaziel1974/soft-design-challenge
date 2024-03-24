package com.cooperative.pollsystem.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "agenda")
@Getter
@Setter
public class Agenda {
        public Agenda(String title, Integer pollDuration, LocalDateTime pollDate) {
                this.title = title;
                this.pollDuration = pollDuration;
                this.pollDate = pollDate;
        }

        @Id
        String id;
        String title;
        Integer pollDuration;
        LocalDateTime pollDate;
}