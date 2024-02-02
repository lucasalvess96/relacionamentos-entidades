package com.lombok.praticas.estudos.patient;

import com.lombok.praticas.estudos.patient.dto.PatientDto;
import com.lombok.praticas.estudos.patient.dto.PatientSearchDto;
import com.lombok.praticas.estudos.patient.swagger.PatientSwagger;
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
public class PatientController implements PatientSwagger {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/create")
    @Transactional
    @Override
    public ResponseEntity<PatientDto> create(@RequestBody @Valid PatientDto patientDto) {
        PatientDto patientCreate = patientService.patientCreate(patientDto);
        return ResponseEntity.created(URI.create("/create/" + patientCreate.id()))
                .body(patientCreate);
    }

    @GetMapping("/pagination")
    @Override
    public ResponseEntity<Page<PatientDto>> patientPagination(@PageableDefault(direction = Sort.Direction.DESC)
                                                              Pageable pageable) {
        return ResponseEntity.ok(patientService.patientPagination(pageable));
    }

    @GetMapping("/list")
    @Override
    public ResponseEntity<List<PatientDto>> patientList() {
        return ResponseEntity.ok(patientService.patientList());
    }

    @PutMapping("/update/{id}")
    @Transactional
    @Override
    public ResponseEntity<PatientDto> update(@PathVariable Long id, @RequestBody @Valid PatientDto patientDto) {
        return ResponseEntity.ok(patientService.patientUpdate(id, patientDto));
    }

    @GetMapping("/detail/{id}")
    @Override
    public ResponseEntity<PatientDto> detail(@PathVariable @Valid Long id) {
        Optional<PatientDto> patientDetailDto = patientService.patientDetail(id);
        return patientDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/search/pagination")
    @Override
    public ResponseEntity<Page<PatientSearchDto>> pagedSearch(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(patientService.searchPatientPagination(name, pageable));
    }

    @GetMapping("/search/list")
    @Override
    public ResponseEntity<List<PatientSearchDto>> searchList(@RequestParam String name) {
        return ResponseEntity.ok(patientService.searchListPerson(name));
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public ResponseEntity<PatientEntity> delete(@PathVariable Long id) {
        patientService.patientDelete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
