package com.lombok.praticas.estudos.student.dto;

import com.lombok.praticas.estudos.course.dto.CourseCreateDto;
import com.lombok.praticas.estudos.student.StudentEntity;

import java.util.Set;

import static com.lombok.praticas.estudos.student.comum.Convert.convertEntityToDto;

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
