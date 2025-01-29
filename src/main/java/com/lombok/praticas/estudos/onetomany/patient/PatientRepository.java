package com.lombok.praticas.estudos.onetomany.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

    Page<PatientEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<PatientEntity> findByNameContainingIgnoreCase(String name);
}
