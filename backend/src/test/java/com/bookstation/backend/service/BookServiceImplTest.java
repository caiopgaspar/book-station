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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        //Verify
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




}
