package com.bookstation.backend.service;

import com.bookstation.backend.dto.request.BookRequest;
import com.bookstation.backend.dto.response.BookResponse;
import com.bookstation.backend.dto.response.PageResponse;

public interface BookService {

    PageResponse<BookResponse> getAllBooks(int page, int size);
    PageResponse<BookResponse> searchBookByTitle(String title, int page, int size);
    BookResponse getBookById(Long id);
    BookResponse createBook(BookRequest request);
    BookResponse updateBook(Long id, BookRequest request);
    void deleteBook(Long id);

}
