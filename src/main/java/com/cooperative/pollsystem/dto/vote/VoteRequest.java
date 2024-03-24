package com.cooperative.pollsystem.dto.vote;

public record VoteRequest(
        //CPF
        String id,
        Boolean vote,
        String pollSessionId) {
}
