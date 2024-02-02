package com.lombok.praticas.estudos.student;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.course.CourseRepository;
import com.lombok.praticas.estudos.course.CourserEntity;
import com.lombok.praticas.estudos.student.dto.StudenCreateDto;
import com.lombok.praticas.estudos.student.dto.StudentListDto;
import com.lombok.praticas.estudos.student.dto.StudentSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.lombok.praticas.estudos.student.comum.Convert.convertDtoToEntity;
import static com.lombok.praticas.estudos.student.comum.Convert.convertEntityToDto;

@Service
public record StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {

    public StudenCreateDto studenCreate(StudenCreateDto studenCreateDto) {
        Set<CourserEntity> courses = convertDtoToEntity(studenCreateDto.courseCreateDto());
        courseRepository.saveAll(courses);
        StudentEntity student = new StudentEntity();
        student.setName(studenCreateDto.name());
        student.setAge(studenCreateDto.age());
        student.setCourses(courses);
        return new StudenCreateDto(studentRepository.save(student));
    }

    public List<StudenCreateDto> studentList() {
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return studentEntities.stream()
                .map(student -> new StudenCreateDto(student.getId(), student.getName(), student.getAge(),
                        convertEntityToDto(student.getCourses()))).toList();
    }

    public Page<StudentListDto> studentPagination(Pageable pageable) {
        Page<StudentEntity> studentEntityPage = studentRepository.findAll(pageable);
        return studentEntityPage.map(student -> new StudentListDto(student.getId(), student.getName(),
                student.getAge(), convertEntityToDto(student.getCourses())));
    }

    public StudenCreateDto updateStudent(Long id, StudenCreateDto studenDto) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(() -> new ErroRequest("Usuário não encontrado"));
        if (studenDto.name() != null) {
            studentEntity.setName(studenDto.name());
        }
        if (studenDto.age() != null) {
            studentEntity.setAge(studenDto.age());
        }
        if (studenDto.courseCreateDto() != null && !studenDto.courseCreateDto()
                .isEmpty()) {
            Set<CourserEntity> updatedCourses = convertDtoToEntity(studenDto.courseCreateDto());
            studentEntity.setCourses(updatedCourses);
        }
        StudentEntity updatedStudent = studentRepository.save(studentEntity);
        return new StudenCreateDto(updatedStudent);
    }

    public Optional<StudenCreateDto> detailStudent(Long id) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(id);
        return studentEntity.map(entity -> Optional.of(new StudenCreateDto(entity)))
                .orElseThrow(() -> new ErroRequest("Usuário não encontrado"));
    }

    public Page<StudentSearchDto> searchStudentPagination(String name, Pageable pageable) {
        Page<StudentEntity> studentEntityPage = studentRepository.findByNameContainingIgnoreCase(name, pageable);
        return studentEntityPage.map(studentEntity -> new StudentSearchDto(studentEntity.getName()));
    }

    public List<StudentSearchDto> searchListStudent(String name) {
        List<StudentEntity> studentEntities = studentRepository.findByNameContainingIgnoreCase(name);
        return studentEntities.stream()
                .map(studentEntity -> new StudentSearchDto(studentEntity.getName())).toList();
    }

    public void deletePerson(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new ErroRequest("Recurso não encontrado");
        }
    }
}
