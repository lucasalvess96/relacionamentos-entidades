package com.lombok.praticas.estudos.persoon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersoonRepository extends JpaRepository<Person, Long> {

    Page<Person> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Person> findByNameContainingIgnoreCase(String name);
}
