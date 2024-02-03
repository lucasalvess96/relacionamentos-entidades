package com.lombok.praticas.estudos.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lombok.praticas.estudos.book.dto.BookDto;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                        .content(new ObjectMapper().writeValueAsString(requestDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Java 101"))
                .andExpect(jsonPath("$.genero").value("Tecnologia"))
                .andExpect(jsonPath("$.title").value("Prog"))
                .andExpect(jsonPath("$.language").value("Java"));
    }

    @Test
    @DisplayName("Should return successfully when creating a book with missing data")
    void createBookWithMissingData() throws Exception {
        BookDto requestDtoWithMissingData = new BookDto("Java 101", null, "Prog", "Java");
        mockMvc.perform(post("/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDtoWithMissingData)))
                .andExpect(status().isCreated());// isBadRequest()
    }

    @Test
    @DisplayName("Should return successfully when creating a book with name empty")
    void createBookWithInvalidData() throws Exception {
        BookDto requestDtoWithInvalidData = new BookDto("", "Tecnologia", "Prog", "Java");
        mockMvc.perform(post("/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
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
        mockMvc.perform(get("/book/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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
}
