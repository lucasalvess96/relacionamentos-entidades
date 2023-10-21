package com.lombok.praticas.estudos.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    boolean existsByName(String name);
    boolean existsByCpf(String cpf);
}
