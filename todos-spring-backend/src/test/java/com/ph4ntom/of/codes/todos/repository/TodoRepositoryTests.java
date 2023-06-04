package com.ph4ntom.of.codes.todos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ph4ntom.of.codes.todos.model.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Testcontainers
class TodoRepositoryTests {

  @Container @ServiceConnection
  static final MongoDBContainer dbContainer = new MongoDBContainer("mongo:5.0.18-focal");

  final Pageable pageable = PageRequest.of(0, 3);

  final Todo todo1 = new Todo("Todo #1AD", "Description #1AD", true);
  final Todo todo2 = new Todo("Todo #2BC", "Description #2BC", false);
  final Todo todo3 = new Todo("Todo #3AD", "Description #3AD", true);

  @Autowired TodoRepository todoRepository;

  @BeforeAll
  static void checkDbContainer() {

    assertThat(dbContainer.isCreated()).isTrue();
    assertThat(dbContainer.isRunning()).isTrue();
  }

  @BeforeEach
  void feedDbContainer() {

    todoRepository.save(todo1);
    todoRepository.save(todo2);
    todoRepository.save(todo3);
  }

  @AfterEach
  void cleanDbContainer() { todoRepository.deleteAll(); }

  @Test
  void shouldReturnTodos_WhichAreDone() {

    final Page<Todo> doneTodos = todoRepository.findByDone(true, pageable);

    assertThat(doneTodos.getNumber()).isZero();
    assertThat(doneTodos.getTotalPages()).isEqualTo(1);
    assertThat(doneTodos.getTotalElements()).isEqualTo(2);
    assertThat(doneTodos.getContent()).hasSize(2).containsOnlyOnce(todo1, todo3);
  }

  @Test
  void shouldReturnTodos_WithTitleContainingString() {

    final Page<Todo> matchedTodos = todoRepository.findByTitleContaining("AD", pageable);

    assertThat(matchedTodos.getNumber()).isZero();
    assertThat(matchedTodos.getTotalPages()).isEqualTo(1);
    assertThat(matchedTodos.getTotalElements()).isEqualTo(2);
    assertThat(matchedTodos.getContent()).hasSize(2).containsOnlyOnce(todo1, todo3);
  }
}
