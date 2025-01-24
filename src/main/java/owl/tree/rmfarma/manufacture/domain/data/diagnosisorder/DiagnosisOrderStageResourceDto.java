package owl.tree.rmfarma.manufacture.domain.data.diagnosisorder;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.infrastructure.entities.DiagnosisPatient;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;
import owl.tree.rmfarma.shared.enumes.StateMachineEnum;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DiagnosisOrderStageResourceDto {
    private String id;
    private String identificationPatient;
    private Integer cycleNumber;
    private Integer cycleDay;
    private OffsetDateTime productionDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private StateMachineEnum status;
    private PatientResourceDto patient;
    private DiagnosisPatientResourceDto diagnosisPatient;
}
