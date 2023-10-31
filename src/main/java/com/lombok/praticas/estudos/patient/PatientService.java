package com.lombok.praticas.estudos.patient;

import com.lombok.praticas.estudos.PatientConsultation.PatientConsultationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    
    public PatientDto patientCreate(PatientDto patientDto) {
        PatientEntity patient = new PatientEntity();
        patient.setName(patientDto.name());
        patient.setAge(patientDto.age());
        patient.setCpf(patientDto.cpf());

        List<PatientConsultationEntity> patientConsultationList = new ArrayList<>();
        if (patientDto.patientConsultationDto() != null) {
            patientConsultationList = patientDto.patientConsultationDto().stream().map(dto -> {
                PatientConsultationEntity patientConsultation = new PatientConsultationEntity();
                patientConsultation.setReason(dto.reason());
                patientConsultation.setPatient(patient);
                return patientConsultation;
            }).collect(Collectors.toList());;
        }
        
        patient.setPatientConsultationList(patientConsultationList);
        return new PatientDto(patientRepository.save(patient));
    }
    
    public Page<PatientDto> patientPagination(Pageable pageable) {
        Page<PatientEntity> patientEntities = patientRepository.findAll(pageable);
        return patientEntities.map(PatientDto::new);
    }
}
