package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.patient.dto.PatientDto;
import com.lombok.praticas.estudos.person.dtoo.PersonCreateDto;
import com.lombok.praticas.estudos.person.dtoo.PersonSearchDto;
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

public interface Personswagger {

    @Operation(summary = "Criar uma nova pessoa", description = "Cria uma nova pessoa caso os campos estiverem " +
            "preenchidos de forma válida",
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
    ResponseEntity<PersonCreateDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo informações da nova pessoa",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PersonCreateDto.class)
                    )
            )
            PersonCreateDto personCreateDto);

    @Operation(summary = "Listar pessoa com paginação",
            description = "Lista quantidade de pessoas por página",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class, allowableValues = "type=PatientDto"),
                                    examples = @ExampleObject(
                                            value = "{\"content\":[{\"id\":1,\"name\":\"john Doe\",\"age\":\"23\"," +
                                                    "\"cpf\":\"12345678951\"}],\"pageable\":{\"pageNumber\":0," +
                                                    "\"pageSize\":10,\"sort\":{\"empty\":true,\"sorted\":false," +
                                                    "\"unsorted\":true},\"offset\":0,\"paged\":true," +
                                                    "\"unpaged\":false},\"totalPages\":1,\"totalElements\":1," +
                                                    "\"last\":true,\"size\":10,\"number\":0,\"sort\":{\"empty\":true," +
                                                    "\"sorted\":false,\"unsorted\":true},\"first\":true," +
                                                    "\"numberOfElements\":1,\"empty\":false}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "204", description = "Não há informações na base de dados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = @ExampleObject(
                                            value = "{\"content\": [],\"pageable\": {\"pageNumber\": 0,\"pageSize\": " +
                                                    "10,\"sort\": {\"empty\": true,\"sorted\": false,\"unsorted\": " +
                                                    "true},\"offset\": 0,\"paged\": true,\"unpaged\": false}," +
                                                    "\"last\": true,\"totalPages\": 0,\"totalElements\": 0,\"size\": " +
                                                    "10,\"number\": 0,\"sort\": {\"empty\": true,\"sorted\": false," +
                                                    "\"unsorted\": true},\"first\": true,\"numberOfElements\": 0," +
                                                    "\"empty\": true}"
                                    )
                            )
                    )
            })
    ResponseEntity<Page<PersonCreateDto>> list(Pageable pageable);

    @Operation(summary = "Listar todas as pessoas", description = "Lista quantidade de pessoas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PatientDto.class))
                            )
                    ),
                    @ApiResponse(responseCode = "204", description = "Não há informações na base dados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErroRequest.class),
                                    examples = @ExampleObject(
                                            value = "[]"
                                    )
                            )
                    )
            })
    ResponseEntity<List<PersonCreateDto>> listing();

    @Operation(summary = "Atualiza uma nova pessoa", description = "método de atualizar",
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
    ResponseEntity<PersonCreateDto> update(
            @Parameter(
                    name = "id",
                    description = "ID da pessoa",
                    in = ParameterIn.PATH,
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            )
            Long id, PersonCreateDto personCreateDto);

    @Operation(summary = "Obter detalhes de uma pessoa pelo ID", description = "busca e informa detalhes da pessoa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes da pessoa encontrado"),
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
    ResponseEntity<PersonCreateDto> detail(
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

    @Operation(summary = "Listar pessoa com paginação",
            description = "Lista quantidade de pessoas por página",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class, allowableValues = "type=PatientDto"),
                                    examples = @ExampleObject(
                                            value = "{\"content\":[{\"name\":\"john Doe\"}]," +
                                                    "\"pageable\":{\"pageNumber\":0," +
                                                    "\"pageSize\":10,\"sort\":{\"empty\":true,\"sorted\":false," +
                                                    "\"unsorted\":true},\"offset\":0,\"paged\":true," +
                                                    "\"unpaged\":false},\"totalPages\":1,\"totalElements\":1," +
                                                    "\"last\":true,\"size\":10,\"number\":0,\"sort\":{\"empty\":true," +
                                                    "\"sorted\":false,\"unsorted\":true},\"first\":true," +
                                                    "\"numberOfElements\":1,\"empty\":false}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "204", description = "Não há informações na base de dados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = @ExampleObject(
                                            value = "{\"content\": [],\"pageable\": {\"pageNumber\": 0,\"pageSize\": " +
                                                    "10,\"sort\": {\"empty\": true,\"sorted\": false,\"unsorted\": " +
                                                    "true},\"offset\": 0,\"paged\": true,\"unpaged\": false}," +
                                                    "\"last\": true,\"totalPages\": 0,\"totalElements\": 0,\"size\": " +
                                                    "10,\"number\": 0,\"sort\": {\"empty\": true,\"sorted\": false," +
                                                    "\"unsorted\": true},\"first\": true,\"numberOfElements\": 0," +
                                                    "\"empty\": true}"
                                    )
                            )
                    )
            })
    ResponseEntity<Page<PersonSearchDto>> pagedSearch(
            @Parameter(
                    name = "nome",
                    description = "nome da pessoa",
                    in = ParameterIn.QUERY,
                    example = "John",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "John")
            )
            String name, Pageable pageable);

    @Operation(summary = "Listar todas as pessoas", description = "Lista quantidade de pessoas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PatientDto.class)),
                                    examples = @ExampleObject(
                                            value = """
                                                        [
                                                            {
                                                                "name": "bosco alves"
                                                            }
                                                        ]
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "204", description = "Não há informações na base dados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErroRequest.class),
                                    examples = @ExampleObject(
                                            value = "[]"
                                    )
                            )
                    )
            })
    ResponseEntity<List<PersonSearchDto>> searchList(
            @Parameter(
                    name = "nome",
                    description = "nome do paciente",
                    in = ParameterIn.QUERY,
                    example = "Doe",
                    required = true,
                    schema = @Schema(type = "string", format = "int64", example = "Doe")
            )
            String name);

    @Operation(summary = "Excluir pessoa pelo ID", description = "exluir pessoa buscada",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pessoa excluída com sucesso"),
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
    ResponseEntity<PersonEntity> delete(
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
