package owl.tree.rmfarma.patient.application.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.patient.application.patient.data.PatientCreateResourceUseCaseDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.api.PatientServicePort;

@Component
@RequiredArgsConstructor
public class CreatePatientUseCase {
    private final PatientServicePort patientServicePort;

    public PatientResourceDto createPatient(PatientCreateResourceUseCaseDto patientCreateResourceUseCaseDto) {
        return patientServicePort.createPatientOrUpdate(patientCreateResourceUseCaseDto);
    }
}
