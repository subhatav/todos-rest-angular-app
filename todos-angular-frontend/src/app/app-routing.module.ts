import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AddTodoComponent } from './components/add-todo/add-todo.component';
import { TodoDetailsComponent } from './components/todo-details/todo-details.component';
import { TodosListComponent } from './components/todos-list/todos-list.component';

const routes: Routes = [
  { path: '', redirectTo: 'todos', pathMatch: 'full' },
  { path: 'add', component: AddTodoComponent },
  { path: 'todos/:id', component: TodoDetailsComponent },
  { path: 'todos', component: TodosListComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
