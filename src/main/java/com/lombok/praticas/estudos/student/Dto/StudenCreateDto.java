package com.lombok.praticas.estudos.student.Dto;

import com.lombok.praticas.estudos.course.Dto.CourseCreateDto;
import com.lombok.praticas.estudos.student.StudentEntity;

import java.util.Set;

import static com.lombok.praticas.estudos.student.comum.convert.convertEntityToDto;

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
