package com.ph4ntom.of.codes.todos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(collection = "todos")
public class Todo {

  @Id private String id;

  private String title;
  private String description;

  private boolean done;

  public Todo(final String title,
              final String description,
              final boolean done) {

    this.title = title;
    this.description = description;
    this.done = done;
  }
}
