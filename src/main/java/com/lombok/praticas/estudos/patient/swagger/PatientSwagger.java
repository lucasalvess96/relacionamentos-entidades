package com.lombok.praticas.estudos.patient.swagger;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.patient.Dto.PatientDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface PatientSwagger {

    @Operation(summary = "Criar uma nova pessoa",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"timestamp\":\"2024-01-15T20:08:06\",\"status\":400," +
                                            "\"error\":\"Erro na informação do campo\",\"message\": \"O campo NOME " +
                                            "não deve está vazio, O campo NOME deve conter apenas letras\"}"
                            )
                    )),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - Erro interno no " +
                            "servidor ao tentar " +
                            "processar requisicao",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErroRequest.class),
                                    examples = @ExampleObject(
                                            value = "{\"timestamp\":\"2024-01-15T20:08:06\",\"status\":500," +
                                                    "\"message\":\"Erro ao enviar informações\"}"
                                    )
                            )
                    )
            })
    ResponseEntity<PatientDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo informações da nova pessoa",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PatientDto.class)
                    )
            )
            @RequestBody @Valid PatientDto patientDto);

    @Operation(summary = "Listar pessoas com paginação",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class, allowableValues = "type=PatientDto")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Não há informações na base dados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErroRequest.class),
                                    examples = @ExampleObject(
                                            value = "[]"
                                    )
                            )
                    )
            })
    ResponseEntity<Page<PatientDto>> patientPagination(Pageable pageable);
}
