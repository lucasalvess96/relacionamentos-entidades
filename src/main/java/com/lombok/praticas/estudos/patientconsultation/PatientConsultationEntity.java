package com.lombok.praticas.estudos.patientconsultation;

import com.lombok.praticas.estudos.patient.PatientEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class PatientConsultationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientConsultationEntity that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getReason(), that.getReason())
                && Objects.equals(getPatient(), that.getPatient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getReason(), getPatient());
    }
}
