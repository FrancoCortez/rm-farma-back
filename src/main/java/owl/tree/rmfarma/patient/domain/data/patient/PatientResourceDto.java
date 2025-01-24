package owl.tree.rmfarma.patient.domain.data.patient;

import lombok.*;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientResourceDto;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PatientResourceDto {
    private String id;
    private String identification;
    private String type;
    private String rut;
    private String name;
    private String lastName;
    private IsapreResourceDto isapre;
    private Set<DiagnosisPatientResourceDto> diagnosisPatient;
}
