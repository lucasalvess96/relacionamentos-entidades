package com.lombok.praticas.estudos.manytomany.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourserEntity, Long> {
}
