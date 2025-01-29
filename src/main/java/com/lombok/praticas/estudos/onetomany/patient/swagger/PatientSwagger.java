package com.lombok.praticas.estudos.onetomany.patient.swagger;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.onetomany.patient.PatientEntity;
import com.lombok.praticas.estudos.onetomany.patient.dto.PatientDto;
import com.lombok.praticas.estudos.onetomany.patient.dto.PatientSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientSwagger {

    @Operation(summary = "Criar um novo paciente", description = "Cria um novo Paciente caso os campos estiverem " +
            "preenchidos de forma válida",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso"),
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
            PatientDto patientDto);

    @Operation(summary = "Listar paciente com paginação", description = "Lista quantidade de pacientes por " +
            "página ",
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

    @Operation(summary = "Listar todas as pessoas", description = "Lista quantidade de pacientes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PatientDto.class))
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
    ResponseEntity<List<PatientDto>> patientList();

    @Operation(summary = "Atualiza uma novo paciente", description = "método de atualizar",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados alterados com sucesso"),
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
    ResponseEntity<PatientDto> update(
            @Parameter(
                    name = "id",
                    description = "ID da pessoa",
                    in = ParameterIn.PATH,
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            )
            Long id, PatientDto patientDto);

    @Operation(summary = "Obter detalhes de um paciente pelo ID", description = "busca e informa detalhes do paciente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes do paciente encontrado"),
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
    ResponseEntity<PatientDto> detail(
            @Parameter(
                    name = "id",
                    description = "ID da pessoa",
                    in = ParameterIn.PATH,
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            )
            Long id
    );

    @Operation(summary = "Pesquisar Paciente com paginação", description = "buscar paciente informado de forma " +
            "paginada",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Página de resultados da pesquisa retornada com " +
                            "sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class, allowableValues = "type" +
                                            "=PatientSearchDto")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"timestamp\":\"2024-01-15T20:08:06\",\"status\":400," +
                                            "\"error\":\"Erro na informação do campo\",\"message\": \"O campo NOME " +
                                            "não deve está vazio, O campo NOME deve conter apenas letras\"}"
                            )
                    )),
            })
    ResponseEntity<Page<PatientSearchDto>> pagedSearch(
            @Parameter(
                    name = "nome",
                    description = "nome do paciente",
                    in = ParameterIn.QUERY,
                    example = "John",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "John")
            )
            String name, Pageable pageable);

    @Operation(summary = "Pesquisar pessoas com lista", description = "informa a listagem",
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
    ResponseEntity<List<PatientSearchDto>> searchList(
            @Parameter(
                    name = "nome",
                    description = "nome do paciente",
                    in = ParameterIn.QUERY,
                    example = "Doe",
                    required = true,
                    schema = @Schema(type = "string", format = "int64", example = "Doe")
            )
            String name);

    @Operation(summary = "Excluir paciente pelo ID", description = "exluir paciente buscado",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Paciente excluída com sucesso"),
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
    ResponseEntity<PatientEntity> delete(
            @Parameter(
                    name = "id",
                    description = "ID da pessoa",
                    in = ParameterIn.PATH,
                    example = "12",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "12")
            )
            Long id);
}
