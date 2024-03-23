package com.cooperative.pollsystem.dto.vote;

import com.cooperative.pollsystem.dto.pollsession.PollSessionResponse;

public record VoteResponse(
        String id,
        Boolean vote,
        PollSessionResponse pollSession){
}