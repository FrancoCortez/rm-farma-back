package owl.tree.rmfarma.patient.application.patient.data;

import lombok.*;
import owl.tree.rmfarma.patient.application.diagnosispatient.data.DiagnosisPatientCreateResourceUseCaseDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PatientCreateResourceUseCaseDto {
    private String rut;
    private String identification;
    private String name;
    private String lastName;
    private String type;
    private String isapre;
    private List<DiagnosisPatientCreateResourceUseCaseDto> diagnosisPatient;
}
