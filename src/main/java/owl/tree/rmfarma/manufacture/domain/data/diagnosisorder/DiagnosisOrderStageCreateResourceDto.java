package owl.tree.rmfarma.manufacture.domain.data.diagnosisorder;

import lombok.*;
import owl.tree.rmfarma.shared.enumes.StateMachineEnum;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DiagnosisOrderStageCreateResourceDto {
    private String patient;
    private String patientIdentification;
    private String diagnosisPatient;
    private OffsetDateTime productionDate;
    private Integer cycleNumber;
    private Integer cycleDay;
    private StateMachineEnum status = StateMachineEnum.CHECK_PATIENT_OK;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
