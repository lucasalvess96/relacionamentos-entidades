package com.lombok.praticas.estudos.book;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.embeddedid.book.BookEntity;
import com.lombok.praticas.estudos.embeddedid.book.BookRepository;
import com.lombok.praticas.estudos.embeddedid.book.BookService;
import com.lombok.praticas.estudos.embeddedid.book.dto.BookDto;
import com.lombok.praticas.estudos.embeddedid.book.dto.BookSearch;
import com.lombok.praticas.estudos.embeddedid.bookid.BookId;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
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

    @ParameterizedTest
    @CsvSource({
            "Clean Code, Desenvolvimento, Prog, Java",
            "Effective Java, Tecnologia, Prog, Java",
            "Refactoring, Desenvolvimento, Prog, Java"
    })
    @DisplayName("Should return the creation of a book with different data")
    void createBookMockWithDifferentData(String name, String genero, String title, String language) {
        BookDto differentBookDto = new BookDto(name, genero, title, language);
        when(bookRepositoryMock.save(any(BookEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        bookServiceMock.createBook(differentBookDto);
        verify(bookRepositoryMock, times(1)).save(any(BookEntity.class));
    }

    @ParameterizedTest
    @CsvSource({
            "Java 101, Tecnologia, Prog, Java",
            "Clean Code, Desenvolvimento, Prog, Java",
            "Data Science 101, Ciência, Data Science, Python"
    })
    @DisplayName("Should return the creation of a book with real data (parametrized)")
    @Transactional
    void createBookRealDataParametrized(String name, String genero, String title, String language) {
        bookService.createBook(new BookDto(name, genero, title, language));
        List<BookEntity> savedBooks = bookRepository.findAll();
        assertNotNull(savedBooks);
        assertEquals(1, savedBooks.size());
        assertEquals(name, savedBooks.get(0).getName());
        assertEquals(genero, savedBooks.get(0).getGenero());
        assertEquals(title, savedBooks.get(0).getBookId().getTitle());
        assertEquals(language, savedBooks.get(0).getBookId().getLanguage());
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
        assertEquals(0, result.getNumberOfElements());
        assertEquals(0, result.getNumber());
        assertEquals(1, result.getTotalPages());
        assertFalse(result.hasPrevious());
        assertFalse(result.hasNext());
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
        assertEquals(0, result.getNumberOfElements());
        assertEquals(0, result.getNumber());
        assertEquals(0, result.getTotalPages());
        assertFalse(result.hasPrevious());
        assertFalse(result.hasNext());
    }

    @Test
    @DisplayName("Should return the updated book in a mocked form")
    void updateBookMock() {
        BookEntity existingBookEntity = new BookEntity(new BookId("1", "Java"), "Java 101", "Tecnologia");
        when(bookRepositoryMock.findById(1L)).thenReturn(Optional.of(existingBookEntity));
        when(bookRepositoryMock.save(any(BookEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        BookDto updatedBookDto = bookServiceMock.updateBook(1L, new BookDto("Updated Book", "New Genre", "New " +
                                                                    "Title", "New Language"
                                                            )
        );
        verify(bookRepositoryMock, times(1)).findById(1L);
        verify(bookRepositoryMock, times(1)).save(any(BookEntity.class));
        assertNotNull(updatedBookDto);
        assertEquals("Updated Book", updatedBookDto.name());
        assertEquals("New Genre", updatedBookDto.genero());
        assertEquals("New Title", updatedBookDto.title());
        assertEquals("New Language", updatedBookDto.language());
    }

    @Test
    @DisplayName("Should throw ErroRequest when book ID not found during update")
    void updateBookNotFoundMock() {
        when(bookRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        ErroRequest erroRequest = assertThrows(ErroRequest.class, () ->
                bookServiceMock.updateBook(1L, this.bookDto)
        );
        assertEquals("informação não encontrada", erroRequest.getMessage());
        verify(bookRepositoryMock).findById(1L);
        verify(bookRepositoryMock, never()).save(any(BookEntity.class));
    }

    @Test
    @DisplayName("Should return the book details when found")
    void detailBookMockSuccess() {
        when(bookRepositoryMock.findByBookIdTitle("Java 101")).thenReturn(Optional.of(this.bookEntity));
        Optional<BookDto> result = bookServiceMock.detailBook("Java 101");
        assertTrue(result.isPresent());
        assertEquals("Java 101", result.get().name());
        assertEquals("Tecnologia", result.get().genero());
    }

    @Test
    @DisplayName("Should throw ErroRequest when book details not found")
    void detailBookMockNotFound() {
        when(bookRepositoryMock.findByBookIdTitle("Nonexistent Book")).thenReturn(Optional.empty());
        ErroRequest erroRequest = assertThrows(ErroRequest.class, () -> bookServiceMock.detailBook("Nonexistent Book"));
        assertEquals("informação não encontrada", erroRequest.getMessage());
    }

    @Test
    @DisplayName("Should return the book details when found")
    void detailBookRealDataSuccess() {
        bookRepository.save(new BookEntity(new BookId("Java 101", "Java"), "Java 101", "Tecnologia"));
        Optional<BookDto> result = bookService.detailBook("Java 101");
        assertTrue(result.isPresent());
        assertEquals("Java 101", result.get().name());
        assertEquals("Tecnologia", result.get().genero());
    }

    @Test
    @DisplayName("Should throw ErroRequest when book details not found")
    void detailBookRealDataNotFound() {
        ErroRequest erroRequest = assertThrows(ErroRequest.class, () -> bookService.detailBook("Nonexistent Book"));
        assertEquals("informação não encontrada", erroRequest.getMessage());
    }

    @Test
    @DisplayName("Should return a paginated list of BookSearch")
    void searchBookMockPagination() {
        String searchName = "Java";
        BookEntity bookEntityTwo = new BookEntity(new BookId("1", "Java"), "Clean Code", "Tecnologia");
        Pageable pageable = Pageable.unpaged();
        Page<BookEntity> bookEntityPage = new PageImpl<>(List.of(this.bookEntity, bookEntityTwo));
        when(bookRepositoryMock.findByNameContainingIgnoreCase(searchName, pageable)).thenReturn(bookEntityPage);
        Page<BookSearch> result = bookServiceMock.searchBookPagination(searchName, pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals("Java 101", result.getContent().get(0).name());
        assertEquals("Clean Code", result.getContent().get(1).name());
    }

    @Test
    @DisplayName("Should return a paginated list of BookSearch")
    void searchFailBookMockPagination() {
        String searchName = "Java";
        Pageable pageable = Pageable.unpaged();
        Page<BookEntity> emptyPage = new PageImpl<>(List.of());
        when(bookRepositoryMock.findByNameContainingIgnoreCase(searchName, pageable)).thenReturn(emptyPage);
        BookService bookService = new BookService(bookRepositoryMock);
        Page<BookSearch> result = bookService.searchBookPagination(searchName, pageable);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getContent().size());
    }

    @Test
    @Transactional
    @DisplayName("Should return a paginated list of BookSearch with real data")
    void searchBookRealPagination() {
        bookRepository.saveAll(List.of(this.bookEntity));
        Page<BookSearch> result = bookService.searchBookPagination("Java 101", Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        assertEquals("Java 101", result.getContent().get(0).name());
    }

    @Test
    @DisplayName("Should return an empty paginated list of BookSearch with real data")
    void searchFailBookRealPagination() {
        Page<BookSearch> result = bookService.searchBookPagination("Nonexistent", Pageable.unpaged());
        assertTrue(result.isEmpty());
        assertEquals(0, result.getContent().size());
    }

    @Test
    @DisplayName("Should return a list of BookSearch")
    void searchListBookMock() {
        BookEntity bookEntityTwo = new BookEntity(new BookId("1", "Java"), "Clean Code", "Tecnologia");
        List<BookEntity> bookEntities = List.of(this.bookEntity, bookEntityTwo);
        when(bookRepositoryMock.findByNameContainingIgnoreCase("Java")).thenReturn(bookEntities);
        List<BookSearch> result = bookServiceMock.searchListBook("Java");
        assertEquals(2, result.size());
        assertEquals("Java 101", result.get(0).name());
        assertEquals("Clean Code", result.get(1).name());
    }

    @Test
    @DisplayName("Should return an empty list of BookSearch")
    void searchFailListBookMock() {
        when(bookRepositoryMock.findByNameContainingIgnoreCase("Java")).thenReturn(Collections.emptyList());
        List<BookSearch> result = bookServiceMock.searchListBook("Java");
        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    @DisplayName("Should return a list of BookSearch with real data")
    void searchListBookReal() {
        bookRepository.saveAll(List.of(this.bookEntity));
        List<BookSearch> result = bookService.searchListBook("Java");
        assertEquals(1, result.size());
        assertEquals("Java 101", result.get(0).name());
    }

    @Test
    @DisplayName("Should return an empty list of BookSearch with real data")
    void searchFailListBookReal() {
        List<BookSearch> result = bookService.searchListBook("Nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should delete book successfully")
    void deleteBookMockSuccess() {
        String title = "Java 101";
        when(bookRepositoryMock.existsByBookIdTitle(title)).thenReturn(true);
        bookServiceMock.deleteBook(title);
        verify(bookRepositoryMock, times(1)).deleteByBookIdTitle(title);
    }

    @Test
    @DisplayName("Should throw ErroRequest when trying to delete non-existent book")
    void deleteBookMockNotFound() {
        String title = "Nonexistent Book";
        when(bookRepositoryMock.existsByBookIdTitle(title)).thenReturn(false);
        ErroRequest erroRequest = assertThrows(ErroRequest.class, () -> bookServiceMock.deleteBook(title));
        assertEquals("Recurso não encontrado", erroRequest.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Should delete book successfully in real data")
    void deleteBookSuccessReal() {
        String title = "1";
        bookRepository.save(this.bookEntity);
        bookService.deleteBook(title);
        assertFalse(bookRepository.existsByBookIdTitle(title));
    }

    @Test
    @DisplayName("Should throw ErroRequest when trying to delete non-existent book in real data")
    void deleteBookNotFoundReal() {
        String title = "Nonexistent Book";
        ErroRequest erroRequest = assertThrows(ErroRequest.class, () -> bookService.deleteBook(title));
        assertEquals("Recurso não encontrado", erroRequest.getMessage());
    }
}
