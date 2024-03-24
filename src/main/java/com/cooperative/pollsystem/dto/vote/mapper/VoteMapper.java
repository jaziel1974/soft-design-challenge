package com.cooperative.pollsystem.dto.vote.mapper;

import com.cooperative.pollsystem.dto.pollsession.mapper.PollSessionMapper;
import com.cooperative.pollsystem.dto.vote.VoteRequest;
import com.cooperative.pollsystem.dto.vote.VoteResponse;
import com.cooperative.pollsystem.model.Vote;

public class VoteMapper {
    public static Vote toDomain(VoteRequest voteRequest) {
        return new Vote(
                voteRequest.id(),
                voteRequest.vote());
    }

    public static VoteResponse toResponse(Vote vote) {
        return new VoteResponse(
                vote.getId(),
                vote.getVote(),
                PollSessionMapper.toResponse(vote.getPollSession()));
    }
}
