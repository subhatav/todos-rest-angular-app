import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { NgxPaginationModule } from 'ngx-pagination';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AddTodoComponent } from './components/add-todo/add-todo.component';
import { TodoDetailsComponent } from './components/todo-details/todo-details.component';
import { TodosListComponent } from './components/todos-list/todos-list.component';

@NgModule({
  declarations: [
    AppComponent,
    AddTodoComponent,
    TodoDetailsComponent,
    TodosListComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
