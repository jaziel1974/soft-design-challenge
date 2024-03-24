package com.cooperative.pollsystem.controller;

import com.cooperative.pollsystem.dto.agenda.AgendaRequest;
import com.cooperative.pollsystem.dto.agenda.AgendaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgendaControllerTest {
    @Autowired
    WebTestClient webTestClient;

    //prepare test
    String title;
    Integer pollDuration;
    LocalDateTime pollDate;
    AtomicReference<AgendaResponse> createdAgenda;

    @BeforeEach
    void setUp() {
        title = "valid title";
        pollDuration = 10;
        pollDate = LocalDateTime.now().plusDays(1);
        createdAgenda = new AtomicReference<>();

        webTestClient
                .delete()
                .uri("/agenda/" + title)
                .exchange();
    }

    @Test
    void testCreateAgenda_FailDuplicatedId() {

        //execute test
        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(title, pollDuration, pollDate))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AgendaResponse.class)
                .value(response -> {
                    // assert response
                    assert response.title().equals(title);
                    assert response.pollDuration() == pollDuration;
                });

        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(title, pollDuration, pollDate))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateAgenda_FailInvalidDate() {
        pollDate = LocalDateTime.now().minusDays(1);
        //execute test
        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(title, pollDuration, pollDate))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateAgenda_FailInvalidPollDuration() {
        pollDuration = -1;
        //execute test
        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(title, pollDuration, pollDate))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateAgenda_Success() {
        //execute test
        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(title, pollDuration, pollDate))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AgendaResponse.class)
                .value(response -> {
                    // assert response
                    assert response.title().equals(title);
                    assert response.pollDuration() == pollDuration;
                });
    }

    @Test
    void testCreateAgenda_SuccessNullDuration() {
        //execute test
        webTestClient
                .post()
                .uri("/agenda")
                .bodyValue(new AgendaRequest(title, null, pollDate))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AgendaResponse.class);
    }

    @Test
    void testGetAgenda_Success() {
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

        //test
        webTestClient
                .get()
                .uri("/agenda/" + createdAgenda.get().id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AgendaResponse.class)
                .value(response ->
                {
                    // assert response
                    assert response.title().equals(title);
                });
    }
}