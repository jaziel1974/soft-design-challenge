package com.cooperative.pollsystem.repository;

import com.cooperative.pollsystem.model.PollSession;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PollSessionRepository extends CrudRepository<PollSession, String>{
    @Query("{'agendaId': ?0}")
    PollSession findByAgendaId(String agendaId);

}
