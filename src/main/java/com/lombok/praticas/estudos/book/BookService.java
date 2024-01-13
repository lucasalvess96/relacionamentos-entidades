package com.lombok.praticas.estudos.book;

import com.lombok.praticas.estudos.book.Dto.BookDto;
import com.lombok.praticas.estudos.book.Dto.BookSearch;
import com.lombok.praticas.estudos.comun.ErroRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lombok.praticas.estudos.book.utils.Converter.convertBookIdfromDto;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDto createBook(BookDto bookDto) {
        BookEntity bookEntity = new BookEntity();
        return getCreateAndUpdateBook(bookEntity, bookDto);
    }

    public List<BookDto> listBook() {
        List<BookEntity> bookEntity = bookRepository.findAll();
        return bookEntity.stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    public Page<BookDto> paginationBook(Pageable pageable) {
        Page<BookEntity> bookEntityPage = bookRepository.findAll(pageable);
        return bookEntityPage.map(BookDto::new);
    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ErroRequest("informação não encontrada"));
        return getCreateAndUpdateBook(bookEntity, bookDto);
    }

    public Optional<BookDto> detailBook(String title) {
        Optional<BookEntity> bookEntity = bookRepository.findByBookIdTitle(title);
        return bookEntity.map(entity -> Optional.of(new BookDto(entity)))
                .orElseThrow(() -> new ErroRequest("informação não encontrada"));
    }

    public Page<BookSearch> searchBookPagination(String name, Pageable pageable) {
        Page<BookEntity> bookEntityPage = bookRepository.findByNameContainingIgnoreCase(name, pageable);
        return bookEntityPage.map(entity -> new BookSearch(entity.getName()));
    }

    public List<BookSearch> searchListBook(String name) {
        List<BookEntity> bookEntities = bookRepository.findByNameContainingIgnoreCase(name);
        return bookEntities.stream()
                .map(entity -> new BookSearch(entity.getName()))
                .collect(Collectors.toList());
    }

    public void deleteBook(String title) {
        if (bookRepository.existsByBookIdTitle(title)) {
            bookRepository.deleteByBookIdTitle(title);
        } else {
            throw new ErroRequest("Recurso não encontrado");
        }
    }

    private BookDto getCreateAndUpdateBook(BookEntity bookEntity, BookDto bookDto) {
        bookEntity.setName(bookDto.name());
        bookEntity.setGenero(bookDto.genero());
        bookEntity.setBookId(convertBookIdfromDto(bookDto));
        bookRepository.save(bookEntity);
        return new BookDto(bookEntity);
    }
}
