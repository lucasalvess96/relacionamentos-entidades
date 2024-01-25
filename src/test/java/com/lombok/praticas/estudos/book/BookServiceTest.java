package com.lombok.praticas.estudos.book;

import com.lombok.praticas.estudos.book.Dto.BookDto;
import com.lombok.praticas.estudos.bookId.BookId;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class BookServiceTest {

    @InjectMocks
    private BookService bookServiceMock;

    @Mock
    private BookRepository bookRepositoryMock;

    private BookEntity bookEntity;

    private BookDto bookDto;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookEntity = new BookEntity(new BookId("1", "Java"), "Java 101", "Tecnologia");
        bookDto = new BookDto("Java 101", "Tecnologia", "Prog", "Java");
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return the creation of a book in a mocked form")
    void createBookMock() {
        when(bookRepositoryMock.save(any(BookEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        bookServiceMock.createBook(this.bookDto);
        verify(bookRepositoryMock, times(1)).save(any(BookEntity.class));
    }

    @Test
    @DisplayName("Should return the creation of a book with real data")
    @Transactional
    void createBookRealData() {
        bookService.createBook(this.bookDto);
        List<BookEntity> savedBooks = bookRepository.findAll();
        assertNotNull(savedBooks);
        assertEquals(1, savedBooks.size());
        assertEquals("Java 101", savedBooks.get(0).getName());
        assertEquals("Tecnologia", savedBooks.get(0).getGenero());
        assertEquals("Prog", savedBooks.get(0).getBookId().getTitle());
        assertEquals("Java", savedBooks.get(0).getBookId().getLanguage());
    }

    @Test
    @DisplayName("Should return the list of a book with data mocked")
    void listBookMock() {
        BookEntity bookEntityTwo = new BookEntity(new BookId("1", "Java"), "Clean Code", "Tecnologia");
        List<BookEntity> bookEntities = Arrays.asList(this.bookEntity, bookEntityTwo);
        when(bookRepositoryMock.findAll()).thenReturn(bookEntities);
        List<BookDto> result = bookServiceMock.listBook();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java 101", result.get(0).name());
        assertEquals("Clean Code", result.get(1).name());
    }

    @Test
    @DisplayName("Should return the list a book with data mocked empty")
    void listBookMockEmptyList() {
        when(bookRepositoryMock.findAll()).thenReturn(Collections.emptyList());
        List<BookDto> result = bookServiceMock.listBook();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    @DisplayName("Should return the List of a book with real data")
    void listBookRealData() {
        bookService.createBook(this.bookDto);
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

    @Test
    @Transactional
    @DisplayName("Should return an empty list when no books are in the real database")
    void listBookRealDataEmptyList() {
        List<BookDto> result = bookService.listBook();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the list of a book with data paged")
    void listBookMockPaged() {
        BookEntity bookEntityTwo = new BookEntity(new BookId("1", "Java"), "Clean Code", "Tecnologia");
        List<BookEntity> bookEntities = Arrays.asList(this.bookEntity, bookEntityTwo);
        Page<BookEntity> bookEntityPage = new PageImpl<>(bookEntities);
        when(bookRepositoryMock.findAll(any(Pageable.class))).thenReturn(bookEntityPage);
        Page<BookDto> result = bookServiceMock.paginationBook(PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Java 101", result.getContent().get(0).name());
        assertEquals("Clean Code", result.getContent().get(1).name());
    }

    @Test
    @DisplayName("Should return an empty page when no books are in the mocked repository for pagination")
    void paginationBookMockEmptyPage() {
        when(bookRepositoryMock.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Page<BookDto> result = bookServiceMock.paginationBook(PageRequest.of(0, 10));
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @Transactional
    @DisplayName("Should return the list of a book with data real paginated")
    void listBookRealDataPaginated() {
        BookEntity bookEntityTwo = new BookEntity(new BookId("2", "Java"), "Clean Code", "Desenvolvimento");
        bookRepository.saveAll(Arrays.asList(bookEntity, bookEntityTwo));
        Page<BookDto> result = bookService.paginationBook(PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Java 101", result.getContent().get(0).name());
        assertEquals("Clean Code", result.getContent().get(1).name());
    }

    @Test
    @DisplayName("Should return an empty page when no books exist in the real repository for pagination")
    void paginationBookRealDataEmptyPage() {
        Page<BookDto> result = bookService.paginationBook(PageRequest.of(0, 10));
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}
