package com.bookstation.backend.service;

import com.bookstation.backend.dto.request.BookRequest;
import com.bookstation.backend.dto.response.BookResponse;
import com.bookstation.backend.dto.response.PageResponse;
import com.bookstation.backend.domain.Book;
import com.bookstation.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private static final int DEFAULT_PAGE_SIZE = 12;

    @Override
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<BookResponse> searchBookByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(this::toResponse);
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return toResponse(book);
    }

    @Override
    public BookResponse createBook(BookRequest request){

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .year(request.getYear())
                .description(request.getDescription())
                .build();

        Book savedBook = bookRepository.save(book);

        return toResponse(savedBook);


    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request){

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setYear(request.getYear());
        book.setDescription(request.getDescription());

        Book updatedBook = bookRepository.save(book);
        return toResponse(updatedBook);
    }

    @Override
    public void deleteBook(Long id){
        bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.deleteById(id);
    }


    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getDescription()
        );
    }
}
