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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest
class TodoControllerTests {

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

    final List<Todo> doneTodos;

    doneTodos = Arrays.asList(new Todo("1", "Todo #1", "Description #1", true),
                              new Todo("3", "Todo #3", "Description #3", true));

    when(todoRepository.findByDone(true)).thenReturn(doneTodos);

    mockMvc.perform(get("/rest/todos/done")).andExpect(status().isOk())
           .andExpect(jsonPath("$.size()").value(doneTodos.size())).andDo(print());
  }

  @Test
  void shouldReturnEmptyListOfTodos_WhenDoneTodosAreAbsent() throws Exception {

    final List<Todo> noTodos = Collections.emptyList();
    final String errorMessage = "List of done Todos is empty!";

    when(todoRepository.findByDone(true)).thenReturn(noTodos);

    mockMvc.perform(get("/rest/todos/done")).andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value(errorMessage)).andDo(print());
  }

  @Test
  void shouldReturnListOfTodos_WhenAllTodosArePresent() throws Exception {

    final List<Todo> allTodos;

    allTodos = Arrays.asList(new Todo("1", "Todo #1", "Description #1", true),
                             new Todo("2", "Todo #2", "Description #2", false),
                             new Todo("3", "Todo #3", "Description #3", true));

    when(todoRepository.findAll()).thenReturn(allTodos);

    mockMvc.perform(get("/rest/todos")).andExpect(status().isOk())
           .andExpect(jsonPath("$.size()").value(allTodos.size())).andDo(print());
  }

  @Test
  void shouldReturnEmptyListOfTodos_WhenNoTodosArePresent() throws Exception {

    final List<Todo> noTodos = Collections.emptyList();
    final String errorMessage = "List of Todos is empty!";

    when(todoRepository.findAll()).thenReturn(noTodos);

    mockMvc.perform(get("/rest/todos")).andExpect(status().isNotFound())
           .andExpect(jsonPath("$.message").value(errorMessage)).andDo(print());
  }

  @Test
  void shouldReturnListOfMatchedTodos_WhichContainSpecificStringInTitle() throws Exception {

    final String title = "AD";
    final List<Todo> matchedTodos;
    final MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();

    matchedTodos = Arrays.asList(new Todo("1", "Todo #1AD", "Description #1AD", true),
                                 new Todo("3", "Todo #3AD", "Description #3AD", true));

    queries.add("title", title);

    when(todoRepository.findByTitleContaining(title)).thenReturn(matchedTodos);

    mockMvc.perform(get("/rest/todos").params(queries)).andExpect(status().isOk())
           .andExpect(jsonPath("$.size()").value(matchedTodos.size())).andDo(print());
  }

  @Test
  void shouldReturnEmptyListOfTodos_WhenNoneContainSpecificStringInTitle() throws Exception {

    final String title = "EF";
    final List<Todo> noTodos = Collections.emptyList();
    final MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();

    queries.add("title", title);

    when(todoRepository.findByTitleContaining(title)).thenReturn(noTodos);

    mockMvc.perform(get("/rest/todos").params(queries)).andExpect(status().isNotFound()).andDo(print());
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
