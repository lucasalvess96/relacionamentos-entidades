package com.lombok.praticas.estudos.manytomany.student.dto;

import com.lombok.praticas.estudos.manytomany.course.dto.CourseCreateDto;
import com.lombok.praticas.estudos.manytomany.student.StudentEntity;

import java.util.Set;

import static com.lombok.praticas.estudos.manytomany.student.comum.Convert.convertEntityToDto;

public record StudenCreateDto(Long id, String name, String age, Set<CourseCreateDto> courseCreateDto) {

    public StudenCreateDto(StudentEntity student) {
        this(
                student.getId(),
                student.getName(),
                student.getAge(),
                convertEntityToDto(student.getCourses())
        );
    }
}
