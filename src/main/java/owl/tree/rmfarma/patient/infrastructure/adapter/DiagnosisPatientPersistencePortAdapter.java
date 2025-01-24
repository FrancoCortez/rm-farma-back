package owl.tree.rmfarma.patient.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.spi.DiagnosisPatientPersistencePort;
import owl.tree.rmfarma.patient.infrastructure.mappers.DiagnosisPatientMapper;
import owl.tree.rmfarma.patient.infrastructure.repository.DiagnosisPatientRepository;

@Component
@RequiredArgsConstructor
public class DiagnosisPatientPersistencePortAdapter implements DiagnosisPatientPersistencePort {
    private final DiagnosisPatientRepository diagnosisPatientRepository;
    private final DiagnosisPatientMapper diagnosisPatientMapper;


    public DiagnosisPatientResourceDto createDiagnosisPatient(DiagnosisPatientCreateResourceDto resource) {
        return this.diagnosisPatientMapper.toDiagnosisPatientResource(this.diagnosisPatientRepository.save(this.diagnosisPatientMapper.toDiagnosisPatientEntity(resource)));
    }

    @Override
    public void patchCycles(Integer cycleNumber, Integer cycleDay, String diagnosisPatient) {
        this.diagnosisPatientRepository.updateCycles(diagnosisPatient, cycleNumber, cycleDay);
    }
}
