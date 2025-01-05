package owl.tree.rmfarma.patient.domain.ports.api;


import owl.tree.rmfarma.patient.application.patient.data.PatientCreateResourceUseCaseDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;

public interface PatientServicePort {
    PatientResourceDto createPatient(PatientCreateResourceUseCaseDto patientCreateResourceUseCaseDto);

    PatientResourceDto findPatientByIdentification(String identification);
}
