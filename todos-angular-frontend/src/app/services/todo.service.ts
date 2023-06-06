import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Todo } from '../models/todo.model';

const apiUrl = '/rest/todos';

@Injectable({
  providedIn: 'root',
})
export class TodoService {
  constructor(private http: HttpClient) {}

  readSpecific(id: any): Observable<Todo> {
    return this.http.get<Todo>(`${apiUrl}/${id}`);
  }

  readAll(params: any): Observable<any> {
    return this.http.get<any>(apiUrl, { params });
  }

  /* readByTitle(title: any): Observable<Todo[]> {
    return this.http.get<Todo[]>(`${apiUrl}?title=${title}`);
  } */

  createNew(data: any): Observable<any> {
    return this.http.post(apiUrl, data);
  }

  updateSpecific(id: any, data: any): Observable<any> {
    return this.http.put(`${apiUrl}/${id}`, data);
  }

  deleteSpecific(id: any): Observable<any> {
    return this.http.delete(`${apiUrl}/${id}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(apiUrl);
  }
}
