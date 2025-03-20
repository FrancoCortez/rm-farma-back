package owl.tree.rmfarma.patient.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.IsaprePersistencePort;
import owl.tree.rmfarma.patient.application.patient.data.PatientCreateResourceUseCaseDto;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.api.DiagnosisPatientServicePort;
import owl.tree.rmfarma.patient.domain.ports.api.PatientServicePort;
import owl.tree.rmfarma.patient.domain.ports.spi.PatientPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.IsEmptyException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientServicePort {

    private final PatientPersistencePort patientPersistencePort;
    private final IsaprePersistencePort isaprePersistencePort;
    private final DiagnosisPatientServicePort diagnosisPatientServicePort;

    @Transactional
    public PatientResourceDto createPatientOrUpdate(PatientCreateResourceUseCaseDto patientCreateResourceUseCaseDto) {
        if (patientCreateResourceUseCaseDto.getIdentification() == null)
            throw new IsEmptyException("Identification", "Patient");
        PatientResourceDto patientResourceDto = this.patientPersistencePort.findPatientByIdentification(patientCreateResourceUseCaseDto.getIdentification());
        PatientCreateResourceDto patientCreateResourceDto = PatientCreateResourceDto.builder()
                .rut(patientCreateResourceUseCaseDto.getRut())
                .type(patientCreateResourceUseCaseDto.getType())
                .identification(patientCreateResourceUseCaseDto.getIdentification())
                .name(patientCreateResourceUseCaseDto.getName())
                .lastName(patientCreateResourceUseCaseDto.getLastName())
                .build();
        if (patientCreateResourceUseCaseDto.getIsapre() != null) {
            IsapreResourceDto isapreResourceDto = this.isaprePersistencePort.findByCode(Integer.parseInt(patientCreateResourceUseCaseDto.getIsapre()));
            if (isapreResourceDto != null) patientCreateResourceDto.setIsapre(isapreResourceDto.getId());
        }
        if (patientResourceDto != null) patientCreateResourceDto.setId(patientResourceDto.getId());
        Set<DiagnosisPatientResourceDto> diagnosisResult = new HashSet<>();
        PatientResourceDto result = this.patientPersistencePort.createPatient(patientCreateResourceDto);
        PatientResourceDto resultDebug = this.patientPersistencePort.findPatientByIdentification(patientCreateResourceUseCaseDto.getIdentification());
        if (patientCreateResourceUseCaseDto.getDiagnosisPatient() != null && !patientCreateResourceUseCaseDto.getDiagnosisPatient().isEmpty()) {
            patientCreateResourceUseCaseDto.getDiagnosisPatient().forEach(f -> {
                diagnosisResult.add(this.diagnosisPatientServicePort.createDiagnosisPatient(f, result));
            });
        }
        result.setDiagnosisPatient(diagnosisResult);
        return result;
    }

    @Override
    public PatientResourceDto findPatientByIdentification(String identification) {
        if (identification == null || identification.isEmpty()) throw new IsEmptyException("Identification", "Patient");
        PatientResourceDto result = this.patientPersistencePort.findPatientByIdentification(identification);
        if (result == null) throw new NotFoundException("Identification", "Patient");
        return result;
    }

    @Override
    public List<PatientResourceDto> findAll() {
        return this.patientPersistencePort.findAll();
    }
}
