package com.lombok.praticas.estudos.student.comum;

import com.lombok.praticas.estudos.course.CourserEntity;
import com.lombok.praticas.estudos.course.Dto.CourseCreateDto;

import java.util.Set;
import java.util.stream.Collectors;

public class convert {

    public static Set<CourseCreateDto> convertEntityToDto(Set<CourserEntity> courseEntities) {
        return courseEntities.stream()
                .map(courseEntity -> new CourseCreateDto(courseEntity.getId(), courseEntity.getName()))
                .collect(Collectors.toSet());
    }

    public static Set<CourserEntity> convertDtoToEntity(Set<CourseCreateDto> courseCreateDtos) {
        return courseCreateDtos.stream()
                .map(courseCreateDto -> {
                    CourserEntity course = new CourserEntity();
                    course.setId(courseCreateDto.id());
                    course.setName(courseCreateDto.name());
                    return course;
                })
                .collect(Collectors.toSet());
    }
}
