package com.cooperative.pollsystem.repository;

import com.cooperative.pollsystem.model.Vote;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, String>{
    @Query("{'pollSessionId': ?0}")
    Vote findByPollSessionId(String pollSessionId);

}
