package com.lombok.praticas.estudos.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Page<BookEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<BookEntity> findByNameContainingIgnoreCase(String name);

    Optional<BookEntity> findByBookIdTitle(String title);

    Boolean existsByBookIdTitle(String title);

    void deleteByBookIdTitle(String title);
}
