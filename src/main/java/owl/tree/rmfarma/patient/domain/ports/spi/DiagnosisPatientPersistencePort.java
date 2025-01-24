package owl.tree.rmfarma.patient.domain.ports.spi;

import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientResourceDto;

public interface DiagnosisPatientPersistencePort {
    DiagnosisPatientResourceDto createDiagnosisPatient(DiagnosisPatientCreateResourceDto resource);

    void patchCycles(Integer cycleNumber, Integer cycleDay, String diagnosisPatient);
}
