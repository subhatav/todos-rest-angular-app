import { Component } from '@angular/core';

import { Todo } from '../../models/todo.model';
import { TodoService } from '../../services/todo.service';

@Component({
  selector: 'app-add-todo',
  templateUrl: './add-todo.component.html',
  styleUrls: ['./add-todo.component.css'],
})
export class AddTodoComponent {
  todo: Todo = {
    title: '',
    description: '',
    done: false,
  };

  submitted = false;

  constructor(private todoService: TodoService) {}

  saveFresh(): void {
    const data = {
      title: this.todo.title,
      description: this.todo.description,
    };

    this.todoService.createNew(data).subscribe({
      next: (response) => {
        console.log(response);

        this.submitted = true;
      },
      error: (exc) => console.error(exc),
    });
  }

  prepareFresh(): void {
    this.submitted = false;

    this.todo = {
      title: '',
      description: '',
      done: false,
    };
  }
}
