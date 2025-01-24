package owl.tree.rmfarma.patient.domain.ports.spi;

import owl.tree.rmfarma.patient.domain.data.patient.PatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;

import java.util.List;

public interface PatientPersistencePort {
    PatientResourceDto createPatient(PatientCreateResourceDto patientCreateResourceDto);

    PatientResourceDto findPatientByIdentification(String identification);

    List<PatientResourceDto> findAll();
}
