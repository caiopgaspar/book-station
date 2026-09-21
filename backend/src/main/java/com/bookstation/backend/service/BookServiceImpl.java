package com.bookstation.backend.service;

import com.bookstation.backend.dto.request.BookRequest;
import com.bookstation.backend.dto.response.BookResponse;
import com.bookstation.backend.domain.Book;
import com.bookstation.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.Year;

@Slf4j
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

        log.debug("Book request details: {}", request);

        try {
            validateYear(request.getYear());
            log.debug("Year validation passed: {}", request.getYear());

            Book book = Book.builder()
                    .title(request.getTitle())
                    .author(request.getAuthor())
                    .year(request.getYear())
                    .description(request.getDescription())
                    .build();

            Book savedBook = bookRepository.save(book);
            log.info("Book created successfully with ID: {}", savedBook.getId());

            return toResponse(savedBook);

        } catch (IllegalArgumentException ex) {
            log.warn("Invalid year provided: {} - {}", request.getYear(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error creating book: {}", ex.getMessage(), ex);
            throw ex;
        }
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
        log.info("Deleting book with ID: {}", id);
        bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.deleteById(id);
        log.info("Book deleted successfully with ID: {}", id);
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

    private void validateYear(Integer year) {

        int currentYear = Year.now().getValue();

        if (year < 1000) {
            throw new IllegalArgumentException("Year must be greater than or equal to 1000");
        }

        if (year > (currentYear + 1)) {
            throw new IllegalArgumentException("Year cannot be greater then next year: " + (currentYear + 1));
        }
    }
}
