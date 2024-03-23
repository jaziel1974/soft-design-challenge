package com.cooperative.pollsystem.repository;

import com.cooperative.pollsystem.model.Vote;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, String>{
    @Query("{'pollSession.Id': ?0, 'id': ?1}")
    Vote findByPollSessionId(String pollSessionId, String id);

}
