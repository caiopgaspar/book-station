package com.bookstation.backend.service;

import com.bookstation.backend.dto.request.BookRequest;
import com.bookstation.backend.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Page<BookResponse> getAllBooks(Pageable pageable);
    Page<BookResponse> searchBookByTitle(String title, Pageable pageable);
    BookResponse getBookById(Long id);
    BookResponse createBook(BookRequest request);
    BookResponse updateBook(Long id, BookRequest request);
    void deleteBook(Long id);

}
