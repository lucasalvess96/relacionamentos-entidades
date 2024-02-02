package com.lombok.praticas.estudos.student;

import com.lombok.praticas.estudos.student.dto.StudenCreateDto;
import com.lombok.praticas.estudos.student.dto.StudentListDto;
import com.lombok.praticas.estudos.student.dto.StudentSearchDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("/student")
@RestController
@CrossOrigin
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<StudenCreateDto> create(@RequestBody @Valid StudenCreateDto createDto) {
        StudenCreateDto studenCreateDto = studentService.studenCreate(createDto);
        return ResponseEntity.created(URI.create("/create" + createDto.id()))
                .body(studenCreateDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<StudenCreateDto>> list() {
        return ResponseEntity.ok()
                .body(studentService.studentList());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<StudentListDto>> pagination(@PageableDefault(direction = Sort.Direction.DESC)
                                                           Pageable pageable) {
        return ResponseEntity.ok()
                .body(studentService.studentPagination(pageable));
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<StudenCreateDto> update(@PathVariable Long id,
                                                  @RequestBody @Valid StudenCreateDto studenCreateDto) {
        StudenCreateDto studentUpdate = studentService.updateStudent(id, studenCreateDto);
        return ResponseEntity.ok()
                .body(studentUpdate);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<StudenCreateDto> detail(@PathVariable @Valid Long id) {
        Optional<StudenCreateDto> personDetailDto = studentService.detailStudent(id);
        return personDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Page<StudentSearchDto>> pagedSearch(@RequestParam String name, Pageable pageable) {
        Page<StudentSearchDto> studentSearchDtos = studentService.searchStudentPagination(name, pageable);
        return ResponseEntity.ok()
                .body(studentSearchDtos);
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<StudentSearchDto>> searchList(@RequestParam String name) {
        List<StudentSearchDto> personSearchDtos = studentService.searchListStudent(name);
        return ResponseEntity.ok()
                .body(personSearchDtos);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<StudentEntity> delete(@PathVariable Long id) {
        studentService.deletePerson(id);
        return ResponseEntity.noContent()
                .build();
    }
}
