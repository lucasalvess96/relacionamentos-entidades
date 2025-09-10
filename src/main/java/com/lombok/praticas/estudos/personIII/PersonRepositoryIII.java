package com.lombok.praticas.estudos.personIII;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepositoryIII extends JpaRepository<PersonEntityIII, Long> {

    Page<PersonEntityIII> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<PersonEntityIII> findByNameContainingIgnoreCase(String name);
}
