package com.ph4ntom.of.codes.todos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ph4ntom.of.codes.todos.model.Todo;
import com.ph4ntom.of.codes.todos.repository.TodoRepository;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest
class TodoControllerTests {

  final Pageable pageable = PageRequest.of(0, 3);

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @MockBean TodoRepository todoRepository;

  @Test
  void shouldReturnMatchedTodo_WhenSpecificTodoIsPresent() throws Exception {

    final String id = "2";

    final Todo specificTodo = new Todo(id, "Todo #2BC", "Description #2BC", false);

    when(todoRepository.findById(id)).thenReturn(Optional.of(specificTodo));

    mockMvc.perform(get("/rest/todos/{id}", id))
           .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
           .andExpect(jsonPath("$.title").value(specificTodo.getTitle()))
           .andExpect(jsonPath("$.description").value(specificTodo.getDescription()))
           .andExpect(jsonPath("$.done").value(specificTodo.isDone())).andDo(print());
  }

  @Test
  void shouldNotReturnAnyTodo_WhenSpecificTodoIsAbsent() throws Exception {

    final String id = "2";
    final String errorMessage = "Todo with [id=" + id + "] is not found!";

    when(todoRepository.findById(id)).thenReturn(Optional.empty());

    mockMvc.perform(get("/rest/todos/{id}", id)).andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value(errorMessage)).andDo(print());
  }

  @Test
  void shouldReturnListOfTodos_WhenDoneTodosArePresent() throws Exception {

    final Todo openingTodo = new Todo("1", "Todo #1", "Description #1", true);
    final Todo closingTodo = new Todo("3", "Todo #3", "Description #3", true);

    final Page<Todo> pageTodos = new PageImpl<>(Arrays.asList(openingTodo, closingTodo));

    when(todoRepository.findByDone(true, pageable)).thenReturn(pageTodos);

    mockMvc.perform(get("/rest/todos/done")).andExpect(status().isOk())
           .andExpect(jsonPath("$.todos.size()").value(2))
           .andExpect(jsonPath("$.todos[0]").value(openingTodo))
           .andExpect(jsonPath("$.todos[1]").value(closingTodo))
           .andExpect(jsonPath("$.totalItems").value(pageTodos.getTotalElements()))
           .andExpect(jsonPath("$.totalPages").value(pageTodos.getTotalPages()))
           .andExpect(jsonPath("$.currentPage").value(pageTodos.getNumber())).andDo(print());
  }

  @Test
  void shouldReturnEmptyListOfTodos_WhenDoneTodosAreAbsent() throws Exception {

    final List<Todo> noTodos = Collections.emptyList();
    final Page<Todo> pageTodos = new PageImpl<>(noTodos);
    final String errorMessage = "List of done Todos is empty!";

    when(todoRepository.findByDone(true, pageable)).thenReturn(pageTodos);

    mockMvc.perform(get("/rest/todos/done")).andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value(errorMessage)).andDo(print());
  }

  @Test
  void shouldReturnListOfTodos_WhenAllTodosArePresent() throws Exception {

    final Todo firstTodo = new Todo("1", "Todo #1", "Description #1", true);
    final Todo secondTodo = new Todo("2", "Todo #2", "Description #2", false);
    final Todo thirdTodo = new Todo("3", "Todo #3", "Description #3", true);

    final Page<Todo> pageTodos = new PageImpl<>(Arrays.asList(firstTodo, secondTodo, thirdTodo));

    when(todoRepository.findAll(pageable)).thenReturn(pageTodos);

    mockMvc.perform(get("/rest/todos")).andExpect(status().isOk())
            .andExpect(jsonPath("$.todos.size()").value(3))
            .andExpect(jsonPath("$.todos[0]").value(firstTodo))
            .andExpect(jsonPath("$.todos[1]").value(secondTodo))
            .andExpect(jsonPath("$.todos[2]").value(thirdTodo))
            .andExpect(jsonPath("$.totalItems").value(pageTodos.getTotalElements()))
            .andExpect(jsonPath("$.totalPages").value(pageTodos.getTotalPages()))
            .andExpect(jsonPath("$.currentPage").value(pageTodos.getNumber())).andDo(print());
  }

  @Test
  void shouldReturnEmptyListOfTodos_WhenNoTodosArePresent() throws Exception {

    final List<Todo> noTodos = Collections.emptyList();
    final Page<Todo> pageTodos = new PageImpl<>(noTodos);
    final String errorMessage = "List of Todos is empty!";

    when(todoRepository.findAll(pageable)).thenReturn(pageTodos);

    mockMvc.perform(get("/rest/todos")).andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value(errorMessage)).andDo(print());
  }

  @Test
  void shouldReturnListOfMatchedTodos_WhichContainSpecificStringInTitle() throws Exception {

    final String title = "AD";
    final MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();

    final Todo openingTodo = new Todo("1", "Todo #1AD", "Description #1AD", true);
    final Todo closingTodo = new Todo("3", "Todo #3AD", "Description #3AD", true);

    final Page<Todo> pageTodos = new PageImpl<>(Arrays.asList(openingTodo, closingTodo));

    queries.add("title", title);

    when(todoRepository.findByTitleContaining(title, pageable)).thenReturn(pageTodos);

    mockMvc.perform(get("/rest/todos").params(queries)).andExpect(status().isOk())
            .andExpect(jsonPath("$.todos.size()").value(2))
            .andExpect(jsonPath("$.todos[0]").value(openingTodo))
            .andExpect(jsonPath("$.todos[1]").value(closingTodo))
            .andExpect(jsonPath("$.totalItems").value(pageTodos.getTotalElements()))
            .andExpect(jsonPath("$.totalPages").value(pageTodos.getTotalPages()))
            .andExpect(jsonPath("$.currentPage").value(pageTodos.getNumber())).andDo(print());
  }

  @Test
  void shouldReturnEmptyListOfTodos_WhenNoneContainSpecificStringInTitle() throws Exception {

    final String title = "EF";
    final List<Todo> noTodos = Collections.emptyList();
    final Page<Todo> pageTodos = new PageImpl<>(noTodos);
    final String errorMessage = "List of Todos is empty!";
    final MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();

    queries.add("title", title);

    when(todoRepository.findByTitleContaining(title, pageable)).thenReturn(pageTodos);

    mockMvc.perform(get("/rest/todos").params(queries)).andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value(errorMessage)).andDo(print());
  }

  @Test
  void shouldCreateNewTodo_ForSuccessfulCreationRequest() throws Exception {

    final Todo freshTodo = new Todo("2", "Todo #2BC", "Description #2BC", false);

    mockMvc.perform(post("/rest/todos").contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(freshTodo))).andExpect(status().isCreated()).andDo(print());
  }

  @Test
  void shouldReturnServerError_ForFailedCreationRequest() throws Exception {

    final Todo freshTodo = new Todo("2", "Todo #2BC", "Description #2BC", false);

    when(todoRepository.save(any(Todo.class))).thenThrow(RuntimeException.class);

    mockMvc.perform(post("/rest/todos").contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(freshTodo)))
           .andExpect(status().isInternalServerError()).andDo(print());
  }

  @Test
  void shouldUpdateMatchedTodo_WhenSpecificTodoIsPresent() throws Exception {

    final String id = "2";

    final Todo agedTodo = new Todo(id, "Todo #2", "Description #2", false);
    final Todo freshTodo = new Todo(id, "Updated Todo #2", "Updated Description #2", true);

    when(todoRepository.findById(id)).thenReturn(Optional.of(agedTodo));
    when(todoRepository.save(any(Todo.class))).thenReturn(freshTodo);

    mockMvc.perform(put("/rest/todos/{id}", id).contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(freshTodo))).andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value(freshTodo.getTitle()))
           .andExpect(jsonPath("$.description").value(freshTodo.getDescription()))
           .andExpect(jsonPath("$.done").value(freshTodo.isDone())).andDo(print());
  }

  @Test
  void shouldNotUpdateAnyTodo_WhenSpecificTodoIsAbsent() throws Exception {

    final String id = "2";
    final String errorMessage = "Todo with [id=" + id + "] is not found!";
    final Todo freshTodo = new Todo(id, "Updated Todo #2", "Updated Description #2", true);

    when(todoRepository.findById(id)).thenReturn(Optional.empty());
    when(todoRepository.save(any(Todo.class))).thenReturn(freshTodo);

    mockMvc.perform(put("/rest/todos/{id}", id).contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(freshTodo))).andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value(errorMessage)).andDo(print());
  }

  @Test
  void shouldDeleteSpecificTodo_ForDeletionRequest() throws Exception {

    final String id = "2";

    doNothing().when(todoRepository).deleteById(id);

    mockMvc.perform(delete("/rest/todos/{id}", id)).andExpect(status().isNoContent()).andDo(print());
  }

  @Test
  void shouldDeleteAllTodos_ForAllDeletionRequest() throws Exception {

    doNothing().when(todoRepository).deleteAll();

    mockMvc.perform(delete("/rest/todos")).andExpect(status().isNoContent()).andDo(print());
  }
}
