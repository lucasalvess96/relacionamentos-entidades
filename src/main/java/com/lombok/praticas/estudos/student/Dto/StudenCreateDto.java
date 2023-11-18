package com.lombok.praticas.estudos.student.Dto;

import com.lombok.praticas.estudos.course.CourserEntity;
import com.lombok.praticas.estudos.course.Dto.CourseCreateDto;
import com.lombok.praticas.estudos.student.StudentEntity;

import java.util.Set;
import java.util.stream.Collectors;

public record StudenCreateDto(Long id, String name, String age, Set<CourseCreateDto> courseCreateDto) {

    public StudenCreateDto(StudentEntity student) {
        this(
                student.getId(),
                student.getName(),
                student.getAge(),
                convertToCreateDto(student.getCourses())
        );
    }

    private static Set<CourseCreateDto> convertToCreateDto(Set<CourserEntity> courses) {
        return courses.stream()
                .map(courserEntity -> new CourseCreateDto(courserEntity.getId(), courserEntity.getName()))
                .collect(Collectors.toSet());
    }
}
