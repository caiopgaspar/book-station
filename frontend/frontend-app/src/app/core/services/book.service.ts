import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API } from '../../shared/Constants/api.constants';

export interface Book {
  id: number;
  title: string;
  author: string;
  year: number;
  description: string;
}

export interface PageResponse<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first?: boolean;
  last?: boolean;
  empty?: boolean;
  numberOfElements?: number;
}

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private apiUrl = API.BASE_URL;

  constructor(private http: HttpClient) {}

  getBooks(page: number = 0, size: number = 12): Observable<PageResponse<Book>> {
    return this.http.get<PageResponse<Book>>(`${this.apiUrl}${API.BOOKS.BASE}`, {
      params: { page: page.toString(), size: size.toString() }
    });
  }

  searchBooks(title: string, page: number = 0, size: number = 12): Observable<PageResponse<Book>> {
    return this.http.get<PageResponse<Book>>(`${this.apiUrl}${API.BOOKS.BASE}`, {
      params: { title, page: page.toString(), size: size.toString() }
    });
  }

  getBook(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}${API.BOOKS.BY_ID(id)}`);
  }

  createBook(book: Partial<Book>): Observable<Book> {
    return this.http.post<Book>(`${this.apiUrl}${API.BOOKS.CREATE}`, book);
  }

  updateBook(id: number, book: Partial<Book>): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}${API.BOOKS.BY_ID(id)}`, book);
  }

  deleteBook(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}${API.BOOKS.BY_ID(id)}`);
  }
}
