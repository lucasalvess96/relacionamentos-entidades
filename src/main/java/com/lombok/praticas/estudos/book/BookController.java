package com.lombok.praticas.estudos.book;

import com.lombok.praticas.estudos.book.Dto.BookDto;
import com.lombok.praticas.estudos.book.Dto.BookSearch;
import com.lombok.praticas.estudos.person.PersonEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookDto> create(@RequestBody @Valid BookDto bookDto) {
        return ResponseEntity.created((URI.create("/create")))
                .body(bookService.createBook(bookDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BookDto>> list() {
        return ResponseEntity.ok(bookService.listBook());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<BookDto>> pagination(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bookService.paginationBook(pageable));
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<BookDto> update(@PathVariable Long id, @RequestBody @Valid BookDto BookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, BookDto));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<BookDto> detail(@PathVariable @Valid Long id) {
        Optional<BookDto> companyDetailDto = bookService.detailBook(id);
        return companyDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Page<BookSearch>> pagedSearch(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(bookService.searchBookPagination(name, pageable));
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<BookSearch>> searchList(@RequestParam String name) {
        return ResponseEntity.ok(bookService.searchListBook(name));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<PersonEntity> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent()
                .build();
    }
}
