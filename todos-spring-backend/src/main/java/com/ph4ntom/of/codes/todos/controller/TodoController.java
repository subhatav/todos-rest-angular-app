package com.ph4ntom.of.codes.todos.controller;

import com.ph4ntom.of.codes.todos.exception.NoResourcesException;
import com.ph4ntom.of.codes.todos.exception.ResourceNotFoundException;
import com.ph4ntom.of.codes.todos.model.Todo;
import com.ph4ntom.of.codes.todos.repository.TodoRepository;
import java.util.*;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
@CrossOrigin(origins = "*")
public class TodoController {

  private final TodoRepository todoRepository;

  private static Supplier<ResourceNotFoundException> getNotFoundException(final String id) {

    return () -> new ResourceNotFoundException("Todo with [id=" + id + "] is not found!");
  }

  private static Map<String, Object> getResponseTodos(final List<Todo> foundTodos,
                                                      final Page<Todo> pageTodos) {

    final Map<String, Object> responseTodos = new HashMap<>();

    responseTodos.put("todos", foundTodos);

    responseTodos.put("currentPage", pageTodos.getNumber());
    responseTodos.put("totalItems", pageTodos.getTotalElements());
    responseTodos.put("totalPages", pageTodos.getTotalPages());

    return responseTodos;
  }

  @GetMapping("/todos/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Todo readSpecificTodo(final @PathVariable("id") String id) {

    return todoRepository.findById(id).orElseThrow(getNotFoundException(id));
  }

  @GetMapping("/todos/done")
  @ResponseStatus(HttpStatus.OK)
  public Map<String, Object> readDoneTodos(final @RequestParam(defaultValue = "0") int page,
                                           final @RequestParam(defaultValue = "3") int size) {

    final Pageable pageable = PageRequest.of(page, size);
    final Page<Todo> pageTodos = todoRepository.findByDone(true, pageable);

    final List<Todo> doneTodos = pageTodos.getContent();

    if (!doneTodos.isEmpty()) return getResponseTodos(doneTodos, pageTodos);
    else throw new NoResourcesException("List of done Todos is empty!");
  }

  @GetMapping("/todos")
  @ResponseStatus(HttpStatus.OK)
  public Map<String, Object> readAllTodos(final @RequestParam(required = false) String title,
                                          final @RequestParam(defaultValue = "0") int page,
                                          final @RequestParam(defaultValue = "3") int size) {

    final List<Todo> allTodos;
    final Page<Todo> pageTodos;

    final Pageable pageable = PageRequest.of(page, size);

    if (Objects.isNull(title)) pageTodos = todoRepository.findAll(pageable);
    else pageTodos = todoRepository.findByTitleContaining(title, pageable);

    allTodos = pageTodos.getContent();

    if (!allTodos.isEmpty()) return getResponseTodos(allTodos, pageTodos);
    else throw new NoResourcesException("List of Todos is empty!");
  }

  @PostMapping("/todos")
  @ResponseStatus(HttpStatus.CREATED)
  public Todo createNewTodo(final @RequestBody Todo freshTodo) {

    return todoRepository.save(new Todo(freshTodo.getTitle(), freshTodo.getDescription(), false));
  }

  @PutMapping("/todos/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Todo updateSpecificTodo(final @PathVariable("id") String id,
                                 final @RequestBody Todo freshTodo) {

    final Todo updatedTodo = todoRepository.findById(id).orElseThrow(getNotFoundException(id));

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
