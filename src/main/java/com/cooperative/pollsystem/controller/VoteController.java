package com.cooperative.pollsystem.controller;

import com.cooperative.pollsystem.dto.vote.VoteRequest;
import com.cooperative.pollsystem.dto.vote.VoteResponse;
import com.cooperative.pollsystem.dto.vote.mapper.VoteMapper;
import com.cooperative.pollsystem.model.Vote;
import com.cooperative.pollsystem.service.VoteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vote")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public VoteResponse create(@RequestBody VoteRequest voteRequest) {
        Vote vote = VoteMapper.toDomain(voteRequest);
        voteService.populatePollSession(vote, voteRequest.pollSessionId());
        Vote createdVote = voteService.create(vote);
        return VoteMapper.toResponse(createdVote);
    }
}