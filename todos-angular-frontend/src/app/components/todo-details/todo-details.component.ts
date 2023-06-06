import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Todo } from '../../models/todo.model';
import { TodoService } from '../../services/todo.service';

@Component({
  selector: 'app-todo-details',
  templateUrl: './todo-details.component.html',
  styleUrls: ['./todo-details.component.css'],
})
export class TodoDetailsComponent implements OnInit {
  @Input() viewMode = false;

  @Input() currentTodo: Todo = {
    title: '',
    description: '',
    done: false,
  };

  message = '';

  constructor(
    private todoService: TodoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.viewMode) {
      this.message = '';

      this.retrieveSpecific(this.route.snapshot.params['id']);
    }
  }

  retrieveSpecific(id: string): void {
    this.todoService.readSpecific(id).subscribe({
      next: (data) => {
        console.log(data);

        this.currentTodo = data;
      },
      error: (exc) => console.error(exc),
    });
  }

  updateStatus(status: boolean): void {
    const data = {
      title: this.currentTodo.title,
      description: this.currentTodo.description,
      done: status,
    };

    this.message = '';

    this.todoService.updateSpecific(this.currentTodo.id, data).subscribe({
      next: (response) => {
        console.log(response);

        this.currentTodo.done = status;

        this.message = response.message
          ? response.message
          : 'The status was updated successfully!';
      },
      error: (exc) => console.error(exc),
    });
  }

  updateData(): void {
    this.message = '';

    this.todoService
      .updateSpecific(this.currentTodo.id, this.currentTodo)
      .subscribe({
        next: (response) => {
          console.log(response);

          this.message = response.message
            ? response.message
            : 'This todo was updated successfully!';
        },
        error: (exc) => console.error(exc),
      });
  }

  removeSpecific(): void {
    this.todoService.deleteSpecific(this.currentTodo.id).subscribe({
      next: (response) => {
        console.log(response);

        this.router.navigate(['/todos']);
      },
      error: (exc) => console.error(exc),
    });
  }
}
