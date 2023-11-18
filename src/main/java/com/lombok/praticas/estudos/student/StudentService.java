package com.lombok.praticas.estudos.student;

import com.lombok.praticas.estudos.course.CourseRepository;
import com.lombok.praticas.estudos.course.CourserEntity;
import com.lombok.praticas.estudos.course.Dto.CourseCreateDto;
import com.lombok.praticas.estudos.student.Dto.StudenCreateDto;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public record StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {

    public StudenCreateDto studenCreate(StudenCreateDto studenCreateDto) {
        StudentEntity student = new StudentEntity();
        student.setName(studenCreateDto.name());
        student.setAge(studenCreateDto.age());
        student.setCourses(convertToCourseEntities(studenCreateDto.courseCreateDto()));
        return new StudenCreateDto(studentRepository.save(student));
    }

    private Set<CourserEntity> convertToCourseEntities(Set<CourseCreateDto> courseCreateDtos) {
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
