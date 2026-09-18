import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BookService, Book, PageResponse } from '../../../core/services/book.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.css'
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  loading = true;
  searchTitle = '';
  currentPage = 0;
  pageSize = 12;
  totalPages = 0;
  totalElements = 0;

  constructor(
    private bookService: BookService,
    private cdr: ChangeDetectorRef,
    private toastr: ToastrService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    console.log('Loading books...');
    this.loading = true;
    this.cdr.detectChanges();

    this.bookService.getBooks(this.currentPage, this.pageSize).subscribe({
      next: (data: PageResponse<Book>) => {
        console.log('Data:', data);
        this.books = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.pageSize = data.size;
        this.totalElements = data.totalElements;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading books:', error);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  searchBooks(): void {
    const title = this.searchTitle.trim();
    if (!title) {
      this.loadBooks();
      return;
    }

    this.loading = true;
    this.cdr.detectChanges();

    this.bookService.searchBooks(title, this.currentPage, this.pageSize).subscribe({
      next: (data: PageResponse<Book>) => {
        this.books = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.pageSize = data.size;
        this.totalElements = data.totalElements;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error searching books:', error);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onSearchClear(): void {
    this.searchTitle = '';
    this.currentPage = 0;
    this.loadBooks();
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) {
      return;
    }
    this.currentPage = page;
    if (this.searchTitle.trim()) {
      this.searchBooks();
    } else {
      this.loadBooks();
    }
  }

  get pageNumbers(): number[] {
    const pages: number[] = [];
    const maxVisible = 5;
    let start = Math.max(0, this.currentPage - Math.floor(maxVisible / 2));
    let end = Math.min(this.totalPages, start + maxVisible);

    if (end - start < maxVisible) {
      start = Math.max(0, end - maxVisible);
    }

    for (let i = start; i < end; i++) {
      pages.push(i);
    }
    return pages;
  }

  deleteBook(book: Book): void {
    const confirmed = window.confirm(`Are you sure you want to delete "${book.title}"?`);
    if (!confirmed) {
      return;
    }

    this.bookService.deleteBook(book.id).subscribe({
      next: () => {
        this.toastr.success('Book deleted successfully');
        this.books = this.books.filter(b => b.id !== book.id);
        this.totalElements--;
        if (this.books.length === 0 && this.currentPage > 0) {
          this.goToPage(this.currentPage - 1);
        }
      },
      error: (error) => {
        console.error('Error deleting book:', error);
        this.toastr.error('Error deleting book');
      }
    });
  }

  getStartIndex(): number {
    return this.currentPage * this.pageSize + 1;
  }

  getEndIndex(): number {
    return Math.min((this.currentPage + 1) * this.pageSize, this.totalElements);
  }

  logout(): void {
    this.authService.logout()
  }

}
