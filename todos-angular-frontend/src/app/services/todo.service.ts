import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Todo } from '../models/todo.model';

const baseUrl = 'http://localhost:8080/rest/todos';

@Injectable({
  providedIn: 'root',
})
export class TodoService {
  constructor(private http: HttpClient) {}

  readSpecific(id: any): Observable<Todo> {
    return this.http.get<Todo>(`${baseUrl}/${id}`);
  }

  readAll(): Observable<Todo[]> {
    return this.http.get<Todo[]>(baseUrl);
  }

  readByTitle(title: any): Observable<Todo[]> {
    return this.http.get<Todo[]>(`${baseUrl}?title=${title}`);
  }

  createNew(data: any): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  updateSpecific(id: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${id}`, data);
  }

  deleteSpecific(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(baseUrl);
  }
}
