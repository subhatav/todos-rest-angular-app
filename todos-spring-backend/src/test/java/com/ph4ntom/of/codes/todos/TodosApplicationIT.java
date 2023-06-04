package com.ph4ntom.of.codes.todos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ph4ntom.of.codes.todos.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodosApplicationIT {

  @Autowired TodoRepository todoRepository;

  @Test
  void shouldHaveThreeTodos_WhenApplicationContextLoads() {

    assertEquals(3, todoRepository.count());
  }
}
