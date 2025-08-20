package owl.tree.rmfarma.patient.application.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.patient.domain.data.patient.PatientComboResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.api.PatientServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindPatientUseCase {
    private final PatientServicePort patientServicePort;

    public PatientResourceDto getPatientByIdentification(String identification) {
        return this.patientServicePort.findPatientByIdentification(identification);
    }

    public List<PatientResourceDto> findAll() {
        return this.patientServicePort.findAll();
    }

    public List<PatientComboResourceDto> getPatientByIdentificationDebound(String identification) {
        return this.patientServicePort.findPatientByIdentificationDebound(identification);
    }
}
