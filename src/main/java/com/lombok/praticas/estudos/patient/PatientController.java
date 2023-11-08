package com.lombok.praticas.estudos.patient;

import com.lombok.praticas.estudos.patient.Dto.PatientDto;
import com.lombok.praticas.estudos.patient.Dto.PatientSearchDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.created(URI.create("/create/" + patientCreate.id()))
                .body(patientCreate);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<PatientDto>> patientPagination(@PageableDefault(direction = Sort.Direction.DESC)
                                                              Pageable pageable) {
        return ResponseEntity.ok()
                .body(patientService.patientPagination(pageable));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PatientDto>> patientList() {
        return ResponseEntity.ok()
                .body(patientService.patientList());
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<PatientDto> update(@PathVariable Long id, @RequestBody @Valid PatientDto patientDto) {
        PatientDto patientUpdate = patientService.patientUpdate(id, patientDto);
        return ResponseEntity.ok()
                .body(patientUpdate);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PatientDto> detail(@PathVariable @Valid Long id) {
        Optional<PatientDto> patientDetailDto = patientService.patientDetail(id);
        return patientDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Page<PatientSearchDto>> pagedSearch(@RequestParam String name, Pageable pageable) {
        Page<PatientSearchDto> personSearchDtos = patientService.searchPatientPagination(name, pageable);
        return ResponseEntity.ok()
                .body(personSearchDtos);
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<PatientSearchDto>> searchList(@RequestParam String name) {
        List<PatientSearchDto> personSearchDtos = patientService.searchListPerson(name);
        return ResponseEntity.ok()
                .body(personSearchDtos);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<PatientEntity> delete(@PathVariable Long id) {
        patientService.patientDelete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
