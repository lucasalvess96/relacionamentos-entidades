package com.lombok.praticas.estudos.patient;

import com.lombok.praticas.estudos.patient.Dto.PatientDto;
import com.lombok.praticas.estudos.patient.Dto.PatientSearchDto;
import com.lombok.praticas.estudos.patient.swagger.PatientSwagger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
                .body(patientService.patientCreate(patientDto));
    }


    @GetMapping("/pagination")
    @Override
    public ResponseEntity<Page<PatientDto>> patientPagination(@PageableDefault(direction = Sort.Direction.DESC)
                                                              Pageable pageable) {
        return ResponseEntity.ok(patientService.patientPagination(pageable));
    }

    @Operation(summary = "Listar todas as pessoas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PatientDto.class))
                            )
                    )
            })
    @GetMapping("/list")
    public ResponseEntity<List<PatientDto>> patientList() {
        return ResponseEntity.ok(patientService.patientList());
    }

    @Operation(summary = "Atualiza uma nova pessoa",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dados alterados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
            })
    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<PatientDto> update(
            @Parameter(
                    name = "id",
                    description = "ID da pessoa",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            )
            @PathVariable Long id, @RequestBody @Valid PatientDto patientDto) {
        return ResponseEntity.ok(patientService.patientUpdate(id, patientDto));
    }

    @Operation(summary = "Obter detalhes de uma pessoa pelo ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes da pessoa encontrados"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content)
            })
    @GetMapping("/detail/{id}")
    public ResponseEntity<PatientDto> detail(
            @Parameter(
                    name = "id",
                    description = "ID da pessoa",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            )
            @PathVariable @Valid Long id) {
        Optional<PatientDto> patientDetailDto = patientService.patientDetail(id);
        return patientDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @Operation(summary = "Pesquisar pessoas com paginação",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Página de resultados da pesquisa retornada com " +
                            "sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class, allowableValues = "type" +
                                            "=PatientSearchDto")
                            )
                    )
            })
    @GetMapping("/search/pagination")
    public ResponseEntity<Page<PatientSearchDto>> pagedSearch(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(patientService.searchPatientPagination(name, pageable));
    }

    @Operation(summary = "Pesquisar pessoas com lista",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lita de resultados da pesquisa retornada com " +
                            "sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class, allowableValues = "type" +
                                            "=PatientSearchDto")
                            )
                    )
            })
    @GetMapping("/search/list")
    public ResponseEntity<List<PatientSearchDto>> searchList(@RequestParam String name) {
        return ResponseEntity.ok(patientService.searchListPerson(name));
    }

    @Operation(summary = "Excluir pessoa pelo ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pessoa excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
            })
    @DeleteMapping("delete/{id}")
    public ResponseEntity<PatientEntity> delete(
            @Parameter(
                    name = "id",
                    description = "ID da pessoa",
                    in = ParameterIn.PATH,
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            )
            @PathVariable Long id) {
        patientService.patientDelete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
