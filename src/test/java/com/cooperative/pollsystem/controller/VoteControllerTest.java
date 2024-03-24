package com.cooperative.pollsystem.controller;

import com.cooperative.pollsystem.dto.agenda.AgendaRequest;
import com.cooperative.pollsystem.dto.agenda.AgendaResponse;
import com.cooperative.pollsystem.dto.pollsession.PollSessionRequest;
import com.cooperative.pollsystem.dto.pollsession.PollSessionResponse;
import com.cooperative.pollsystem.dto.vote.VoteRequest;
import com.cooperative.pollsystem.dto.vote.VoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VoteControllerTest {
    @Autowired
    WebTestClient webTestClient;

    AtomicReference<AgendaResponse> createdAgenda = new AtomicReference<>();
    AtomicReference<PollSessionResponse> createdPoll = new AtomicReference<>();


    @BeforeEach
    void setUp() {
        //prepare test
        var pollUuid = UUID.randomUUID();
        var agendaId = UUID.randomUUID();
        var pollDuration = 10;

        var agendaTitle = "votes test";
        var pollDate = LocalDateTime.now().plusDays(1);

        //delete agenda for test if exists
        webTestClient
                .delete()
                .uri("/agenda/" + agendaTitle)
                .exchange()
                .expectStatus().is2xxSuccessful();

        //create agenda for test
        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(agendaTitle, null, pollDate))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(AgendaResponse.class)
                .value(createdAgenda::set);

        //create poll for test
        webTestClient
                .post()
                .uri("/pollSession")
                .bodyValue(new PollSessionRequest(createdAgenda.get().id(), pollDuration))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PollSessionResponse.class)
                .value(createdPoll::set);
    }

    @Test
    void testCreateVote_Success() {
        var voteId = "82781475076";
        var voteValue = true;

        //test
        webTestClient
                .post()
                .uri("/vote")
                .bodyValue(new VoteRequest(voteId, voteValue, createdPoll.get().id()))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(VoteResponse.class)
                .value(voteResponse -> {
                    assert voteResponse.pollSession().votesYes() == 1;
                });
    }

    @Test
    void testCreateVote_Fail_DuplicatedVote() {
        var voteId = "82781475076";
        var voteValue = true;

        //test
        webTestClient
                .post()
                .uri("/vote")
                .bodyValue(new VoteRequest(voteId, voteValue, createdPoll.get().id()))
                .exchange()
                .expectStatus().is2xxSuccessful();
        webTestClient
                .post()
                .uri("/vote")
                .bodyValue(new VoteRequest(voteId, voteValue, createdPoll.get().id()))
                .exchange()
                .expectStatus().is5xxServerError();
    }
}