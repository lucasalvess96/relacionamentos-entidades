package com.lombok.praticas.estudos.patient;

import com.lombok.praticas.estudos.patient.Dto.PatientDto;
import com.lombok.praticas.estudos.patient.Dto.PatientSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Criar uma nova pessoa",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
            })
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<PatientDto> create(@RequestBody @Valid PatientDto patientDto) {
        PatientDto patientCreate = patientService.patientCreate(patientDto);
        return ResponseEntity.created(URI.create("/create/" + patientCreate.id()))
                .body(patientService.patientCreate(patientDto));
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<PatientDto>> patientPagination(@PageableDefault(direction = Sort.Direction.DESC)
                                                              Pageable pageable) {
        return ResponseEntity.ok(patientService.patientPagination(pageable));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PatientDto>> patientList() {
        return ResponseEntity.ok(patientService.patientList());
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<PatientDto> update(@PathVariable Long id, @RequestBody @Valid PatientDto patientDto) {
        return ResponseEntity.ok(patientService.patientUpdate(id, patientDto));
    }

    @Operation(summary = "Obter detalhes de uma pessoa pelo ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes da pessoa encontrados"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content)
            })
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
        return ResponseEntity.ok(patientService.searchPatientPagination(name, pageable));
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<PatientSearchDto>> searchList(@RequestParam String name) {
        return ResponseEntity.ok(patientService.searchListPerson(name));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<PatientEntity> delete(@PathVariable Long id) {
        patientService.patientDelete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
