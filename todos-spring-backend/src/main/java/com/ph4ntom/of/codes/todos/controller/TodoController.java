package com.ph4ntom.of.codes.todos.controller;

import com.ph4ntom.of.codes.todos.model.Todo;
import com.ph4ntom.of.codes.todos.repository.TodoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@RequestMapping("/rest")
public class TodoController {

  private final TodoRepository todoRepository;

  @GetMapping("/todos")
  public ResponseEntity<List<Todo>> readAllTodos(final @RequestParam
                                    (required = false) String title) {
    try {

      final List<Todo> todos = new ArrayList<>();

      if (Objects.isNull(title)) todos.addAll(todoRepository.findAll());
      else todos.addAll(todoRepository.findByTitleContaining(title));

      if (todos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

      return new ResponseEntity<>(todos, HttpStatus.OK);

    } catch (final Exception exc) {

      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/todos/{id}")
  public ResponseEntity<Todo> readSpecificTodo(final @PathVariable("id") String id) {

    final Optional<Todo> todoData = todoRepository.findById(id);

    return todoData.map(todo -> new ResponseEntity<>(todo, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/todos/done")
  public ResponseEntity<List<Todo>> readDoneTodos() {

    try {

      final List<Todo> todos = todoRepository.findByDone(true);

      if (todos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

      return new ResponseEntity<>(todos, HttpStatus.OK);

    } catch (final Exception exc) {

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/todos")
  public ResponseEntity<Todo> createNewTodo(final @RequestBody Todo todo) {

    try {

      final Todo savedTodo = todoRepository.save(new Todo(todo.getTitle(),
                                            todo.getDescription(), false));

      return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);

    } catch (final Exception exc) {

      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/todos/{id}")
  public ResponseEntity<Todo> updateSpecificTodo(final @PathVariable("id") String id,
                                                 final @RequestBody Todo todo) {

    final Optional<Todo> todoData = todoRepository.findById(id);

    if (todoData.isPresent()) {

      final Todo updatedTodo = todoData.get();

      updatedTodo.setTitle(todo.getTitle());
      updatedTodo.setDescription(todo.getDescription());
      updatedTodo.setDone(todo.isDone());

      return new ResponseEntity<>(todoRepository.save(updatedTodo), HttpStatus.OK);

    } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/todos/{id}")
  public ResponseEntity<HttpStatus> deleteSpecificTodo(final @PathVariable("id") String id) {

    try {

      todoRepository.deleteById(id);

      return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    } catch (final Exception exc) {

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/todos")
  public ResponseEntity<HttpStatus> deleteAllTodos() {

    try {

      todoRepository.deleteAll();

      return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    } catch (final Exception exc) {

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
