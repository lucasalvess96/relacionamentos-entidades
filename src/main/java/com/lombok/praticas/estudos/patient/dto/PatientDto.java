package com.lombok.praticas.estudos.patient.dto;

import com.lombok.praticas.estudos.patient.PatientEntity;
import com.lombok.praticas.estudos.patientconsultation.PatientConsultationDto;
import com.lombok.praticas.estudos.patientconsultation.PatientConsultationEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.stream.Collectors;

public record PatientDto(
        @Schema(description = "id da pessoa", example = "1")
        Long id,
        @Schema(description = "nome da pessoa", example = "john doe")
        @NotBlank(message = "O campo NOME não deve está vazio")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "O campo NOME deve conter apenas letras")
        String name,
        @Schema(description = "campo idade", example = "27")
        @NotBlank(message = "O campo IDADE não deve está vazio")
        @Size(min = 2, max = 2, message = "O campo IDADE deve possuir apenas 2 dígitos")
        @Pattern(regexp = "\\d+", message = "O campo IDADE deve conter apenas números")
        String age,
        @NotBlank(message = "O campo CPF não deve está vazio")
        @Size(min = 11, max = 11, message = "O campo CPF deve possuir apenas 11 dígitos")
        @Pattern(regexp = "\\d+", message = "O campo CPF deve conter apenas números")
        String cpf,
        @Valid
        List<PatientConsultationDto> patientConsultationDto
) {

    public PatientDto(PatientEntity patient) {
        this(
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getCpf(),
                convertToPatientConsultationDtos(patient.getPatientConsultationList())
        );
    }

    private static List<PatientConsultationDto> convertToPatientConsultationDtos(List<PatientConsultationEntity> patientConsultationList) {
        return patientConsultationList.stream()
                .map(patientConsultation -> new PatientConsultationDto(patientConsultation.getId(),
                        patientConsultation.getReason()))
                .collect(Collectors.toList());
    }
}
