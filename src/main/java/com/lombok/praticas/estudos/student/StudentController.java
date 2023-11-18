package com.lombok.praticas.estudos.student;

import com.lombok.praticas.estudos.student.Dto.StudenCreateDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
}
