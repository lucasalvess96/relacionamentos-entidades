package com.lombok.praticas.estudos.student.Dto;

import com.lombok.praticas.estudos.course.Dto.CourseCreateDto;

import java.util.Set;

public record StudentListDto(Long id, String name, String age, Set<CourseCreateDto> courseCreateDto) {

}
