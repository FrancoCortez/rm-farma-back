package owl.tree.rmfarma.patient.domain.data.diagnosispatient;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DiagnosisPatientCreateResourceDto {
    private String id;
    private String identificationPatient;
    private String patientId;
    private Integer cycleNumber;
    private Integer cycleDay;
    private String doctor;
    private String services;
    private String diagnosis;
    private String schema;
    private String hospitalUnit;
}
