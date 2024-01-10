package com.lombok.praticas.estudos.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Page<BookEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<BookEntity> findByNameContainingIgnoreCase(String name);
}
