package owl.tree.rmfarma.patient.application.diagnosispatient.data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DiagnosisPatientCreateResourceUseCaseDto {
    private String id;
    private String doctor;
    private Integer cycleNumber;
    private Integer cycleDay;
    private String schema;
    private String diagnosis;
    private String services;
    private String hospitalUnit;
}
