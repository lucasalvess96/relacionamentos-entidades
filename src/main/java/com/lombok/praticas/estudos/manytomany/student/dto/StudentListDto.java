package com.lombok.praticas.estudos.manytomany.student.dto;

import com.lombok.praticas.estudos.manytomany.course.dto.CourseCreateDto;

import java.util.Set;

public record StudentListDto(Long id, String name, String age, Set<CourseCreateDto> courseCreateDto) {

}
