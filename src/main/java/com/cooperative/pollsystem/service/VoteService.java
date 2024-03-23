package com.cooperative.pollsystem.service;

import com.cooperative.pollsystem.model.PollSession;
import com.cooperative.pollsystem.model.Vote;
import com.cooperative.pollsystem.model.exceptions.DuplicatedIdException;
import com.cooperative.pollsystem.model.exceptions.GenericErrorException;
import com.cooperative.pollsystem.repository.PollSessionRepository;
import com.cooperative.pollsystem.repository.VoteRepository;

import java.util.Optional;
import java.util.UUID;

public class VoteService {
    private final VoteRepository voteRepository;
    private final PollSessionRepository pollSessionRepository;

    public VoteService(VoteRepository voteRepository, PollSessionRepository pollSessionRepository) {
        this.voteRepository = voteRepository;
        this.pollSessionRepository = pollSessionRepository;
    }

    public Vote create(Vote vote) {
        if(vote.getPollSession() == null)
            throw new GenericErrorException("Poll session is required");

        Vote lastVote = voteRepository.findByPollSessionId(vote.getPollSession().getId());
        if(lastVote != null)
            throw new DuplicatedIdException("Associate already voted in this poll session");

        if(vote.getVote())
            vote.getPollSession().setVotesYes(vote.getPollSession().getVotesYes() != null ? vote.getPollSession().getVotesYes() + 1 : 1);
        else
            vote.getPollSession().setVotesNo(vote.getPollSession().getVotesNo() != null ? vote.getPollSession().getVotesNo() + 1 : 1);

        vote.setId(UUID.randomUUID().toString());
        pollSessionRepository.save(vote.getPollSession());
        return voteRepository.save(vote);
    }

    public void populatePollSession(Vote vote, String pollSessionId) {
        Optional<PollSession> pollSessionOptional = pollSessionRepository.findById(pollSessionId);
        pollSessionOptional.orElseThrow(() -> new GenericErrorException("Poll session not found"));
        vote.setPollSession(pollSessionOptional.get());
    }
}