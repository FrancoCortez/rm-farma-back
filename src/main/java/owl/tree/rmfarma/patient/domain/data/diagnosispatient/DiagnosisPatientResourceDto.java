package owl.tree.rmfarma.patient.domain.data.diagnosispatient;

import lombok.*;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DiagnosisPatientResourceDto {
    private String id;
    private String identificationPatient;
    private Integer cycleNumber;
    private Integer cycleDay;
    private DoctorResourceDto doctor;
    private ServiceResourceDto services;
    private DiagnosisResourceDto diagnosis;
    private SchemaResourceDto schema;
    private HospitalUnitResourceDto hospitalUnit;
}
