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

  todos: Todo[] = [];
  currentTodo: Todo = {};

  currentIndex = -1;

  page = 1;
  count = 0;

  pageSize = 3;
  pageSizes = [3, 6, 9];

  constructor(private todoService: TodoService) {}

  ngOnInit(): void {
    this.retrieveAll();
  }

  getParams(searchTitle: string, page: number, pageSize: number): any {
    let params: any = {};

    if (searchTitle) {
      params[`title`] = searchTitle;
    }

    if (page) {
      params[`page`] = page - 1;
    }
    if (pageSize) {
      params[`size`] = pageSize;
    }

    return params;
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

  handlePageChange(event: number): void {
    this.page = event;

    this.retrieveAll();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;

    this.retrieveAll();
  }

  retrieveAll(): void {
    const params = this.getParams(this.title, this.page, this.pageSize);

    this.todoService.readAll(params).subscribe({
      next: (data) => {
        const { todos, totalItems } = data;

        this.todos = todos;
        this.count = totalItems;

        console.log(data);
      },
      error: (exc) => console.error(exc),
    });
  }

  searchTitle(): void {
    this.page = 1;

    this.retrieveAll();
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
