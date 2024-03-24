package com.cooperative.pollsystem.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "votes")
@Getter
@Setter
public class Vote {
    public Vote(String id, Boolean vote) {
        this.id = id;
        this.vote = vote;
    }

    @Id //CPF
    private String id;
    private Boolean vote;
    private PollSession pollSession;
}
