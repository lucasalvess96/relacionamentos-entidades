package com.lombok.praticas.estudos.patient;

import com.lombok.praticas.estudos.PatientConsultation.PatientConsultationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    private String name;

    private String age;

    private String cpf;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PatientConsultationEntity> patientConsultationList = new ArrayList<>();
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientEntity patient)) return false;
        return Objects.equals(getId(), patient.getId()) && Objects.equals(getName(), patient.getName())
                && Objects.equals(getAge(), patient.getAge()) && Objects.equals(getCpf(), patient.getCpf())
                && Objects.equals(getPatientConsultationList(), patient.getPatientConsultationList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAge(), getCpf(), getPatientConsultationList());
    }

    @Override
    public String toString() {
        return "PatientEntity{" + "id=" + id + ", name='" + name + '\'' + ", age='" + age + '\''
                + ", cpf='" + cpf + '\'' + ", patientConsultationList=" + patientConsultationList + '}';
    }
}
