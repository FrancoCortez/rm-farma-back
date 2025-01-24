package owl.tree.rmfarma.patient.domain.ports.api;


import owl.tree.rmfarma.patient.application.patient.data.PatientCreateResourceUseCaseDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;

import java.util.List;

public interface PatientServicePort {
    PatientResourceDto createPatientOrUpdate(PatientCreateResourceUseCaseDto patientCreateResourceUseCaseDto);

    PatientResourceDto findPatientByIdentification(String identification);

    List<PatientResourceDto> findAll();
}
