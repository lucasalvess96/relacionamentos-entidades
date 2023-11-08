package com.lombok.praticas.estudos.patient;

import com.lombok.praticas.estudos.PatientConsultation.PatientConsultationEntity;
import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.patient.Dto.PatientDto;
import com.lombok.praticas.estudos.patient.Dto.PatientSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientDto patientCreate(PatientDto patientDto) {
        PatientEntity patientEntity = new PatientEntity();
        return mapPatientDtoToEntity(patientDto, patientEntity);
    }

    public Page<PatientDto> patientPagination(Pageable pageable) {
        Page<PatientEntity> patientEntities = patientRepository.findAll(pageable);
        return patientEntities.map(PatientDto::new);
    }

    public List<PatientDto> patientList() {
        List<PatientEntity> patientEntityList = patientRepository.findAll();
        return patientEntityList.stream()
                .map(PatientDto::new)
                .collect(Collectors.toList());
    }

    public PatientDto patientUpdate(Long id, PatientDto patientDto) {
        PatientEntity patientEntity = patientRepository.findById(id)
                .orElseThrow(() -> new ErroRequest("usuário não encontrado"));
        return mapPatientDtoToEntity(patientDto, patientEntity);
    }


    public Optional<PatientDto> patientDetail(Long id) {
        Optional<PatientEntity> patientEntity = patientRepository.findById(id);
        return patientEntity.map(entity -> Optional.of(new PatientDto(entity)))
                .orElseThrow(() -> new ErroRequest("Usuário não encontrado"));
    }

    public Page<PatientSearchDto> searchPatientPagination(String name, Pageable pageable) {
        Page<PatientEntity> patientEntityPage = patientRepository.findByNameContainingIgnoreCase(name, pageable);
        return patientEntityPage.map(personEntity -> new PatientSearchDto(personEntity.getName()));
    }

    public List<PatientSearchDto> searchListPerson(String name) {
        List<PatientEntity> patientEntityList = patientRepository.findByNameContainingIgnoreCase(name);
        return patientEntityList.stream()
                .map(patientEntity -> new PatientSearchDto(patientEntity.getName()))
                .collect(Collectors.toList());
    }

    public void patientDelete(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new ErroRequest("Recurso não encontrado");
        }
    }

    private PatientDto mapPatientDtoToEntity(PatientDto patientDto, PatientEntity patientEntity) {
        patientEntity.setName(patientDto.name());
        patientEntity.setAge(patientDto.age());
        patientEntity.setCpf(patientDto.cpf());

        List<PatientConsultationEntity> patientConsultationList = patientEntity.getPatientConsultationList();
        if (patientDto.patientConsultationDto() != null) {
            patientConsultationList.addAll(patientDto.patientConsultationDto()
                    .stream()
                    .map(dto -> {
                        PatientConsultationEntity patientConsultation = new PatientConsultationEntity();
                        patientConsultation.setId(dto.id());
                        patientConsultation.setReason(dto.reason());
                        patientConsultation.setPatient(patientEntity);
                        return patientConsultation;
                    })
                    .toList());
        }
        return new PatientDto(patientRepository.save(patientEntity));
    }
}
