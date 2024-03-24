package com.cooperative.pollsystem.controller;

import com.cooperative.pollsystem.dto.agenda.AgendaRequest;
import com.cooperative.pollsystem.dto.agenda.AgendaResponse;
import com.cooperative.pollsystem.dto.pollsession.PollSessionRequest;
import com.cooperative.pollsystem.dto.pollsession.PollSessionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PollSessionControllerTest {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    TestRestTemplate restTemplate;
    AgendaResponse createdAgenda;
    Integer pollDuration = null;
    String title = null;
    LocalDateTime pollDate = null;
    AtomicReference<PollSessionResponse> createdPollSession = new AtomicReference<>();

    @BeforeEach
    void setUp() {
        title = "poll session test";
        pollDate = LocalDateTime.now().plusDays(1);
        pollDuration = 10;

        restTemplate.delete("/agenda/" + title);

        createdAgenda = restTemplate.postForObject("/agenda", new AgendaRequest(title, null, pollDate), AgendaResponse.class);
    }

    @Test
    void testCreatePoll_Success() {
        //execute test
        webTestClient
                .post()
                .uri("/pollSession")
                .bodyValue(new PollSessionRequest(createdAgenda.id(), pollDuration))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PollSessionResponse.class)
                .value(response ->
                {
                    // assert response
                    assert response.agenda().title().equals(title);
                });
    }

    @Test
    void testCreatePoll_Fail_PollStillOpened() {
        //prepare test
        AtomicReference<AgendaResponse> createdAgenda = new AtomicReference<>();

        //create agenda for test
        webTestClient
                .delete()
                .uri("/agenda/" + title)
                .exchange();
        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(title, null, pollDate))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(AgendaResponse.class)
                .value(createdAgenda::set);

        //create first poll session
        webTestClient
                .post()
                .uri("/pollSession")
                .bodyValue(new PollSessionRequest(createdAgenda.get().id(), pollDuration))
                .exchange()
                .expectStatus().is2xxSuccessful();

        //execute test
        webTestClient
                .post()
                .uri("/pollSession")
                .bodyValue(new PollSessionRequest(createdAgenda.get().id(), pollDuration))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testGetPoll_Success() {
        //prepare test
        AtomicReference<AgendaResponse> createdAgenda = new AtomicReference<>();

        //create agenda for test
        webTestClient
                .delete()
                .uri("/agenda/" + title)
                .exchange();
        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(title, null, pollDate))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(AgendaResponse.class)
                .value(createdAgenda::set);
        //create poll session for test
        webTestClient
                .post()
                .uri("/pollSession")
                .bodyValue(new PollSessionRequest(createdAgenda.get().id(), pollDuration))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PollSessionResponse.class)
                .value(response -> {
                    createdPollSession.set(response);
                });
        //execute test
        webTestClient
                .get()
                .uri("/pollSession/" + createdPollSession.get().id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PollSessionResponse.class)
                .value(response ->
                {
                    // assert response
                    assert response.agenda().title().equals(title);
                });
    }
}