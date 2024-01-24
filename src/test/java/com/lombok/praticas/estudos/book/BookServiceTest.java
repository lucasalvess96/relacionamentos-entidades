package com.lombok.praticas.estudos.book;

import com.lombok.praticas.estudos.book.Dto.BookDto;
import com.lombok.praticas.estudos.bookId.BookId;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookServiceMock;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return the creation of a book in a mocked form")
    void createBookMock() {
        BookRepository bookRepositoryMock = mock(BookRepository.class);
        BookService bookServiceMock = new BookService(bookRepositoryMock);
        BookDto bookDto = new BookDto("Java 101", "Tecnologia", "Prog", "Java");
        when(bookRepositoryMock.save(any(BookEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        bookServiceMock.createBook(bookDto);
        verify(bookRepositoryMock, times(1)).save(any(BookEntity.class));
    }

    @Test
    @DisplayName("Should return the creation of a book with real data")
    @Transactional
    void createBookRealData() {
        BookDto bookDto = new BookDto("Java 101", "Tecnologia", "Prog", "Java");
        bookService.createBook(bookDto);
        List<BookEntity> savedBooks = bookRepository.findAll();
        assertNotNull(savedBooks);
        assertEquals(1, savedBooks.size());
        assertEquals("Java 101", savedBooks.get(0).getName());
        assertEquals("Tecnologia", savedBooks.get(0).getGenero());
        assertEquals("Prog", savedBooks.get(0).getBookId().getTitle());
        assertEquals("Java", savedBooks.get(0).getBookId().getLanguage());
    }

    @Test
    @DisplayName("Should return the list of a book with dados mocked")
    void listBookMock() {
        List<BookEntity> bookEntities = Arrays.asList(
                new BookEntity(new BookId("1", "Java"), "Java 101", "Tecnologia"),
                new BookEntity(new BookId("2", "Java"), "Clean Code", "Desenvolvimento")
        );
        when(bookRepository.findAll()).thenReturn(bookEntities);
        List<BookDto> result = bookServiceMock.listBook();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java 101", result.get(0).name());
        assertEquals("Clean Code", result.get(1).name());
    }

    @Test
    @Transactional
    @DisplayName("Should return the List of a book with real data")
    void listBookRealData() {
        bookService.createBook(new BookDto("Java 101", "Tecnologia", "Prog", "Java"));
        bookService.createBook(new BookDto("Clean Code", "Desenvolvimento", "Prog", "Java"));
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                List<BookDto> result = bookService.listBook();
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals("Java 101", result.get(0).name());
                assertEquals("Clean Code", result.get(1).name());
            }
        });
    }
}
