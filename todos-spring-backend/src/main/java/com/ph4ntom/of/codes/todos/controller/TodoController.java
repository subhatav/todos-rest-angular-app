package com.ph4ntom.of.codes.todos.controller;

import com.ph4ntom.of.codes.todos.exception.NoResourcesException;
import com.ph4ntom.of.codes.todos.exception.ResourceNotFoundException;
import com.ph4ntom.of.codes.todos.model.Todo;
import com.ph4ntom.of.codes.todos.repository.TodoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@RequestMapping("/rest")
public class TodoController {

  private final TodoRepository todoRepository;

  private static Supplier<ResourceNotFoundException> getNotFoundException(final String id) {

    return () -> new ResourceNotFoundException("Todo with [id=" + id + "] is not found!");
  }

  @GetMapping("/todos/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Todo readSpecificTodo(final @PathVariable("id") String id) {

    return todoRepository.findById(id).orElseThrow(getNotFoundException(id));
  }

  @GetMapping("/todos/done")
  @ResponseStatus(HttpStatus.OK)
  public List<Todo> readDoneTodos() {

    final List<Todo> doneTodos = todoRepository.findByDone(true);

    if (!doneTodos.isEmpty()) return doneTodos;
    else throw new NoResourcesException("List of done Todos is empty!");
  }

  @GetMapping("/todos")
  @ResponseStatus(HttpStatus.OK)
  public List<Todo> readAllTodos(final @RequestParam
                    (required = false) String title) {

    final List<Todo> allTodos = new ArrayList<>();

    if (Objects.isNull(title)) allTodos.addAll(todoRepository.findAll());
    else allTodos.addAll(todoRepository.findByTitleContaining(title));

    if (!allTodos.isEmpty()) return allTodos;
    else throw new NoResourcesException("List of Todos is empty!");
  }

  @PostMapping("/todos")
  @ResponseStatus(HttpStatus.CREATED)
  public Todo createNewTodo(final @RequestBody Todo freshTodo) {

    return todoRepository.save(new Todo(freshTodo.getTitle(),
                          freshTodo.getDescription(), false));
  }

  @PutMapping("/todos/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Todo updateSpecificTodo(final @PathVariable("id") String id,
                                 final @RequestBody Todo freshTodo) {

    final Todo updatedTodo = todoRepository.findById(id).orElseThrow
                                           (getNotFoundException(id));

    updatedTodo.setTitle(freshTodo.getTitle());
    updatedTodo.setDescription(freshTodo.getDescription());
    updatedTodo.setDone(freshTodo.isDone());

    return todoRepository.save(updatedTodo);
  }

  @DeleteMapping("/todos/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteSpecificTodo(final @PathVariable("id") String id) {

    todoRepository.deleteById(id);
  }

  @DeleteMapping("/todos")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllTodos() { todoRepository.deleteAll(); }
}
