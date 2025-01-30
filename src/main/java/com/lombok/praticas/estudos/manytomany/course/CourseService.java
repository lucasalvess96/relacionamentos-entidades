package com.lombok.praticas.estudos.manytomany.course;

import com.lombok.praticas.estudos.manytomany.course.dto.CourseCreateDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.lombok.praticas.estudos.manytomany.student.comum.Convert.convertDtoToEntity;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Set<CourserEntity> saveCourse(Set<CourseCreateDto> courseCreateDtos) {
        Set<CourserEntity> courses = convertDtoToEntity(courseCreateDtos);
        return new HashSet<>(courseRepository.saveAll(courses));
    }
}
