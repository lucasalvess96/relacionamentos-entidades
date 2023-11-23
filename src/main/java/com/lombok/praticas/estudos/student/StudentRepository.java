package com.lombok.praticas.estudos.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    Page<StudentEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<StudentEntity> findByNameContainingIgnoreCase(String name);
}
