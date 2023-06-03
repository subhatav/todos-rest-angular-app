package com.ph4ntom.of.codes.todos.repository;

import com.ph4ntom.of.codes.todos.model.Todo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

  List<Todo> findByDone(final boolean done);

  List<Todo> findByTitleContaining(final String title);
}
