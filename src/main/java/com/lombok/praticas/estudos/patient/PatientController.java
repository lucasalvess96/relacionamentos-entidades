package com.lombok.praticas.estudos.patient;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("patient")
@CrossOrigin
public class PatientController {
    
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<PatientDto> create(@RequestBody @Valid PatientDto patientDto) {
        PatientDto patientCreate = patientService.patientCreate(patientDto);
        return ResponseEntity.created(URI.create("/create/" + patientCreate.id())).body(patientCreate);
    }
    
    @GetMapping("/pagination")
    public ResponseEntity<Page<PatientDto>> patientPagination(@PageableDefault(direction =
            Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(patientService.patientPagination(pageable));
    }
}
