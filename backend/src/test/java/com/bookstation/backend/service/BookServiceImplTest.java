package com.bookstation.backend.service;

import com.bookstation.backend.domain.Book;
import com.bookstation.backend.dto.request.BookRequest;
import com.bookstation.backend.dto.response.BookResponse;
import com.bookstation.backend.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Test book")
                .author("Author test")
                .year(2026)
                .description("Description test")
                .build();

        bookRequest = new BookRequest();
        bookRequest.setTitle(book.getTitle());
        bookRequest.setAuthor(book.getAuthor());
        bookRequest.setYear(book.getYear());
        bookRequest.setDescription(book.getDescription());
    }

    @Test
    void getAllBooks_shouldReturnPageOfBookResponses() {
        //Arrange
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        //Act
        Page<BookResponse> result = bookService.getAllBooks(PageRequest.of(0, 12));
        //Assert
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test book");
        assertThat(result.getContent().get(0).getAuthor()).isEqualTo("Author test");
        verify(bookRepository).findAll(any(Pageable.class));
    }

    @Test
    void getBookById_shouldReturnBookResponseWhenFound() {
        //Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        //Act
        BookResponse result = bookService.getBookById(1L);
        //Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test book");
    }

    @Test
    void getBookById_shouldThrowRuntimeExceptionWhenNotFound() {
        //Arrange
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        //Act
        //Assert
        assertThatThrownBy(() -> bookService.getBookById(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found");
    }

    @Test
    void createBook_shouldSaveAndReturnBookResponse() {
        //Arrange
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        //Act
        BookResponse result = bookService.createBook(bookRequest);
        //Assert
        assertThat(result.getTitle()).isEqualTo("Test book");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_shouldThrowIllegalArgumentExceptionWhenYearIsTooOld() {
        //Arrange
        bookRequest.setYear(999);
        //Act
        //Assert
        assertThatThrownBy(() -> bookService.createBook(bookRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Year must be greater than or equal to 1000");
    }

    @Test
    void createBook_shouldThrowIllegalArgumentExceptionWhenYearIsInTheFuture() {
        //Arrange
        bookRequest.setYear(2100);
        //Act
        //Assert
        assertThatThrownBy(() -> bookService.createBook(bookRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Year cannot be greater then next year");
    }

    @Test
    void updateBook_shouldUpdateAndReturnBookResponse() {
        //Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        //Act
        BookResponse result = bookService.updateBook(1L, bookRequest);
        //Assert
        assertThat(result.getTitle()).isEqualTo("Test book");
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(book);
    }

    @Test
    void updateBook_shouldThrowRuntimeExceptionWhenBookNotFound() {
        //Arrange
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        //Act
        //Assert
        assertThatThrownBy(() -> bookService.updateBook(2L, bookRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found");
    }

    @Test
    void deleteBook_shouldDeleteBookWhenFound() {
        //Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(1L);
        //Act
        bookService.deleteBook(1L);
        //Assert
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_shouldThrowRuntimeExceptionWhenBookNotFound() {
        //Arrange
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        //Act
        //Assert
        assertThatThrownBy(() -> bookService.deleteBook(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found");
    }

    @Test
    void searchBookByTitle_shouldReturnMatchingBooks() {
        //Arrange
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findByTitleContainingIgnoreCase(eq("Book"), any(Pageable.class)))
                .thenReturn(bookPage);
        //Act
        Page<BookResponse> result = bookService.searchBookByTitle("Book", PageRequest.of(0, 12));
        //Assert
        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test book");
    }

}
