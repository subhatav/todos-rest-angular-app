package com.ph4ntom.of.codes.todos.repository;

import com.ph4ntom.of.codes.todos.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

  Page<Todo> findByDone(final boolean done, final Pageable pageable);

  Page<Todo> findByTitleContaining(final String title, final Pageable pageable);
}
