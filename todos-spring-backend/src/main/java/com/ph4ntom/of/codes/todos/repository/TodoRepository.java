package com.ph4ntom.of.codes.todos.repository;

import com.ph4ntom.of.codes.todos.model.Todo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TodoRepository extends MongoRepository<Todo, String> {

  List<Todo> findByTitleContaining(final String title);

  List<Todo> findByDone(final boolean done);
}
