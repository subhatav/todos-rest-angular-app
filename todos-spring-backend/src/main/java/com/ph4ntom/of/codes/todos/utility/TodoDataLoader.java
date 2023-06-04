package com.ph4ntom.of.codes.todos.utility;

import com.ph4ntom.of.codes.todos.model.Todo;
import com.ph4ntom.of.codes.todos.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoDataLoader implements CommandLineRunner {

  private final TodoRepository todoRepository;

  @Override
  public void run(final String... arguments) {

    if (todoRepository.count() < 1) {

      final Todo todo1 = new Todo("Learn Spring", "Get some backend knowledge!", true);
      final Todo todo2 = new Todo("Learn React", "Get some frontend knowledge!", false);
      final Todo todo3 = new Todo("Learn Docker", "Get some CI/CD knowledge!", true);

      todoRepository.save(todo1);
      todoRepository.save(todo2);
      todoRepository.save(todo3);
    }
  }
}
