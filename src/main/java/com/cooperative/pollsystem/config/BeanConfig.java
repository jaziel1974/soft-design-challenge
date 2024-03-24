package com.cooperative.pollsystem.config;

import com.cooperative.pollsystem.repository.AgendaRepository;
import com.cooperative.pollsystem.repository.PollSessionRepository;
import com.cooperative.pollsystem.repository.VoteRepository;
import com.cooperative.pollsystem.service.AgendaService;
import com.cooperative.pollsystem.service.PollSessionService;
import com.cooperative.pollsystem.service.VoteService;
import com.cooperative.pollsystem.service.cpfvalidator.CpfValidatorClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class BeanConfig {
    @Bean
    AgendaService agendaService(AgendaRepository agendaRepository) {
        return new AgendaService(agendaRepository);
    }
    @Bean
    PollSessionService pollSessionService(AgendaRepository agendaRepository, PollSessionRepository pollSessionRepository) {
        return new PollSessionService(agendaRepository, pollSessionRepository);
    }
    @Bean
    VoteService voteService(PollSessionRepository pollSessionRepository, VoteRepository voteRepository, CpfValidatorClient cpfValidatorClient) {
        return new VoteService(voteRepository, pollSessionRepository, cpfValidatorClient);
    }
}