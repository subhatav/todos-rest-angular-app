import { Component } from '@angular/core';

import { Todo } from '../../models/todo.model';
import { TodoService } from '../../services/todo.service';

@Component({
  selector: 'app-todos-list',
  templateUrl: './todos-list.component.html',
  styleUrls: ['./todos-list.component.css'],
})
export class TodosListComponent {
  title = '';

  todos?: Todo[];
  currentTodo: Todo = {};

  currentIndex = -1;

  constructor(private todoService: TodoService) {}

  ngOnInit(): void {
    this.retrieveAll();
  }

  setActive(todo: Todo, index: number): void {
    this.currentTodo = todo;
    this.currentIndex = index;
  }

  refreshList(): void {
    this.retrieveAll();

    this.currentTodo = {};
    this.currentIndex = -1;
  }

  retrieveAll(): void {
    this.todoService.readAll().subscribe({
      next: (data) => {
        console.log(data);
        this.todos = data;
      },
      error: (exc) => console.error(exc),
    });
  }

  searchTitle(): void {
    this.currentTodo = {};
    this.currentIndex = -1;

    this.todoService.readByTitle(this.title).subscribe({
      next: (data) => {
        console.log(data);
        this.todos = data;
      },
      error: (exc) => console.error(exc),
    });
  }

  removeAll(): void {
    this.todoService.deleteAll().subscribe({
      next: (response) => {
        console.log(response);
        this.refreshList();
      },
      error: (exc) => console.error(exc),
    });
  }
}
