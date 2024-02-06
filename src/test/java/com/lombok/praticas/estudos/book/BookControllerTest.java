package com.lombok.praticas.estudos.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lombok.praticas.estudos.book.dto.BookDto;
import com.lombok.praticas.estudos.book.dto.BookSearch;
import com.lombok.praticas.estudos.comun.ErroRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private BookDto requestDto;

    private BookDto responseDto;

    private List<BookDto> bookDtoList;

    @BeforeEach
    void setUp() {
        requestDto = new BookDto("Java 101", "Tecnologia", "Prog", "Java");
        responseDto = new BookDto("Java 101", "Tecnologia", "Prog", "Java");
        bookDtoList = Arrays.asList(
                new BookDto("Java 101", "Tecnologia", "Prog", "Java"),
                new BookDto("Clean Code", "Desenvolvimento", "Prog", "Java")
        );
    }

    @Test
    @DisplayName("Should create a book successfully")
    void create() throws Exception {
        when(bookService.createBook(any(BookDto.class))).thenReturn(responseDto);
        mockMvc.perform(post("/book/create").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Java 101"))
                .andExpect(jsonPath("$.genero").value("Tecnologia"))
                .andExpect(jsonPath("$.title").value("Prog"))
                .andExpect(jsonPath("$.language").value("Java"));
    }

    @Test
    @DisplayName("Should return successfully when creating a book with missing data")
    void createBookWithMissingData() throws Exception {
        BookDto requestDtoWithMissingData = new BookDto("Java 101", null, "Prog", "Java");
        mockMvc.perform(post("/book/create").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDtoWithMissingData)))
                .andExpect(status().isCreated());// isBadRequest()
    }

    @Test
    @DisplayName("Should return successfully when creating a book with name empty")
    void createBookWithInvalidData() throws Exception {
        BookDto requestDtoWithInvalidData = new BookDto("", "Tecnologia", "Prog", "Java");
        mockMvc.perform(post("/book/create").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDtoWithInvalidData)))
                .andExpect(status().isCreated()); //isBadRequest
    }

    @Test
    @DisplayName("Should return a list of books")
    void listBooks() throws Exception {
        when(bookService.listBook()).thenReturn(bookDtoList);
        mockMvc.perform(get("/book/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Java 101"))
                .andExpect(jsonPath("$[1].name").value("Clean Code"));
    }

    @Test
    @DisplayName("Should return an empty list when no books are in the database")
    void listEmptyBooks() throws Exception {
        when(bookService.listBook()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/book/list").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should return paginated list of books")
    void paginationBooks() throws Exception {
        Page<BookDto> bookDtoPage = new PageImpl<>(bookDtoList, PageRequest.of(0, 10), bookDtoList.size());
        when(bookService.paginationBook(any(Pageable.class))).thenReturn(bookDtoPage);
        mockMvc.perform(get("/book/pagination").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Java 101"))
                .andExpect(jsonPath("$.content[1].name").value("Clean Code"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.hasPrevious").doesNotExist());
    }


    @Test
    @DisplayName("Should return an empty paginated list when no books are in the database")
    void paginationEmptyBooks() throws Exception {
        Page<BookDto> emptyBookDtoPage = Page.empty();
        when(bookService.paginationBook(any(Pageable.class))).thenReturn(emptyBookDtoPage);
        mockMvc.perform(get("/book/pagination").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @DisplayName("Should update a book successfully")
    void updateBook() throws Exception {
        when(bookService.updateBook(any(Long.class), any(BookDto.class))).thenReturn(responseDto);
        mockMvc.perform(put("/book/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java 101"))
                .andExpect(jsonPath("$.genero").value("Tecnologia"))
                .andExpect(jsonPath("$.title").value("Prog"))
                .andExpect(jsonPath("$.language").value("Java"));
    }

    @Test
    @DisplayName("Should return an error when updating with non-existing ID")
    void updateBookNonExistingId() throws Exception {
        when(bookService.updateBook(any(Long.class), any(BookDto.class)))
                .thenThrow(new ErroRequest("Informação não encontrada"));
        mockMvc.perform(put("/book/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Informação não encontrada"));
    }

    @Test
    @DisplayName("Should return book details when title is found")
    void detailSuccess() throws Exception {
        when(bookService.detailBook(any(String.class))).thenReturn(Optional.of(requestDto));
        mockMvc.perform(get("/book/detail/{title}", "Java 101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java 101"))
                .andExpect(jsonPath("$.genero").value("Tecnologia"))
                .andExpect(jsonPath("$.title").value("Prog"))
                .andExpect(jsonPath("$.language").value("Java"));
    }

    @Test
    @DisplayName("Should return 404 when title is not found")
    void detailNotFound() throws Exception {
        when(bookService.detailBook(any(String.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/book/detail/{title}", "informação não encontrada"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return book search")
    void testSearchWithResults() throws Exception {
        when(bookService.searchBookPagination(any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new BookSearch("java jpa"))));
        mockMvc.perform(get("/book/search/pagination")
                        .param("name", "java")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("java jpa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should return book search empty")
    void testSearchWithNoResults() throws Exception {
        when(bookService.searchBookPagination(any(String.class), any(Pageable.class)))
                .thenReturn(Page.empty());
        mockMvc.perform(get("/book/search/pagination")
                        .param("name", "nonexistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("Should return book search without pagination with results")
    void testSearchWithoutPaginationWithResults() throws Exception {
        when(bookService.searchListBook(any(String.class)))
                .thenReturn(Collections.singletonList(new BookSearch("java jpa")));
        mockMvc.perform(get("/book/search/list")
                        .param("name", "java")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Should return book search without pagination empty")
    void testSearchWithoutPaginationWithNoResults() throws Exception {
        when(bookService.searchListBook(any(String.class))).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/book/search/list")
                        .param("name", "nonexistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Should delete a book successfully")
    void deleteBookSuccessfully() throws Exception {
        mockMvc.perform(delete("/book/delete/{title}", "Java 101"))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteBook("Java 101");
    }

    @Test
    @DisplayName("Should return 'Not Found' when trying to delete a non-existing book")
    void deleteNonExistingBook() throws Exception {
        doThrow(new ErroRequest("Recurso não encontrado")).when(bookService).deleteBook("Recurso não encontrado");
        mockMvc.perform(delete("/book/delete/{title}", "Recurso não encontrado"))
                .andExpect(status().isNotFound());
    }
}
