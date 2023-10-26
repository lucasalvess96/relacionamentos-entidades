package com.lombok.praticas.estudos.person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    boolean existsByName(String name);
    
    boolean existsByCpf(String cpf);

    Page<PersonEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<PersonEntity> findByNameContainingIgnoreCase(String name);
}
