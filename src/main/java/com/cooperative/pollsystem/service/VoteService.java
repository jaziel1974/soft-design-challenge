package com.cooperative.pollsystem.service;

import com.cooperative.pollsystem.dto.cpfvalidator.CpfValidatorResponse;
import com.cooperative.pollsystem.model.PollSession;
import com.cooperative.pollsystem.model.Vote;
import com.cooperative.pollsystem.model.exceptions.DuplicatedIdException;
import com.cooperative.pollsystem.model.exceptions.GenericErrorException;
import com.cooperative.pollsystem.repository.PollSessionRepository;
import com.cooperative.pollsystem.repository.VoteRepository;
import com.cooperative.pollsystem.service.cpfvalidator.CpfValidatorClient;
import com.cooperative.pollsystem.service.cpfvalidator.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Optional;

public class VoteService {
    Logger logger = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;
    private final PollSessionRepository pollSessionRepository;
    private final CpfValidatorClient cpfValidatorClient;
    @Value("${cpf-validator.validate-cpf}")
    private boolean IS_VALIDATE_CPF_ENABLED;

    public VoteService(VoteRepository voteRepository, PollSessionRepository pollSessionRepository, CpfValidatorClient cpfValidatorClient) {
        this.voteRepository = voteRepository;
        this.pollSessionRepository = pollSessionRepository;
        this.cpfValidatorClient = cpfValidatorClient;
    }

    public Vote create(Vote vote) {
        if (vote.getPollSession() == null) {
            logger.error("Poll session is required");
            throw new GenericErrorException("Poll session is required");
        }

        if (vote.getPollSession().getValidUntil().isBefore(LocalDateTime.now())) {
            logger.error("Poll session is closed");
            throw new GenericErrorException("Poll session is closed");
        }

        if (vote.getId() == null) {
            logger.error("Id (CPF) is required");
            throw new GenericErrorException("Id (CPF) is required");
        }

        if (IS_VALIDATE_CPF_ENABLED){
            try {
                CpfValidatorResponse cpfValidatorResponse = cpfValidatorClient.validateCpf(vote.getId());
                if (cpfValidatorResponse.status() == Status.UNABLE_TO_VOTE){
                    logger.error("Associate is not able to vote");
                    throw new GenericErrorException("Associate is not able to vote");
                }
            } catch (Exception e) {
                logger.error("Error validating CPF", e);
                throw new GenericErrorException("Error validating CPF");
            }
        }

        Vote alreadyVoted = voteRepository.findByPollSessionId(vote.getPollSession().getId(), vote.getId());
        if (alreadyVoted != null) {
            logger.error("Associate already voted in this poll session");
            throw new DuplicatedIdException("Associate already voted in this poll session");
        }

        if (vote.getVote())
            vote.getPollSession().setVotesYes(vote.getPollSession().getVotesYes() != null ? vote.getPollSession().getVotesYes() + 1 : 1);
        else
            vote.getPollSession().setVotesNo(vote.getPollSession().getVotesNo() != null ? vote.getPollSession().getVotesNo() + 1 : 1);

        pollSessionRepository.save(vote.getPollSession());
        return voteRepository.save(vote);
    }

    public void populatePollSession(Vote vote, String pollSessionId) {
        Optional<PollSession> pollSessionOptional = pollSessionRepository.findById(pollSessionId);
        pollSessionOptional.orElseThrow(() -> new GenericErrorException("Poll session not found"));
        vote.setPollSession(pollSessionOptional.get());
    }
}