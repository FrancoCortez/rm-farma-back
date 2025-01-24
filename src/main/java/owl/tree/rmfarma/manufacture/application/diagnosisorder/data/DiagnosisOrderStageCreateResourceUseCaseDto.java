package owl.tree.rmfarma.manufacture.application.diagnosisorder.data;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DiagnosisOrderStageCreateResourceUseCaseDto {
    private String patientIdentification;
    private OffsetDateTime productionDate;
    private Integer cycleNumber;
    private Integer cycleDay;
    private String diagnosisPatient;
    private String patient;
}
