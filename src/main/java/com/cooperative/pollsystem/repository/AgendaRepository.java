package com.cooperative.pollsystem.repository;

import com.cooperative.pollsystem.model.Agenda;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AgendaRepository extends CrudRepository<Agenda, String> {
    @Query(value="{'title' : ?0}")
    Agenda findByTitle(String title);

    @Query(value="{'title' : ?0}", delete = true)
    void delete(String title);
}
