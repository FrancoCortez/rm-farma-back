package owl.tree.rmfarma.patient.domain.ports.api;

import owl.tree.rmfarma.patient.application.diagnosispatient.data.DiagnosisPatientCreateResourceUseCaseDto;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;

public interface DiagnosisPatientServicePort {

    DiagnosisPatientResourceDto createDiagnosisPatient(DiagnosisPatientCreateResourceUseCaseDto entry, PatientResourceDto patient);
}
