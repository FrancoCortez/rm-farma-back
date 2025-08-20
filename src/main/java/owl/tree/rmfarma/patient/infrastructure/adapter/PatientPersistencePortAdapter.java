package owl.tree.rmfarma.patient.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import owl.tree.rmfarma.patient.domain.data.patient.PatientComboResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.spi.PatientPersistencePort;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;
import owl.tree.rmfarma.patient.infrastructure.mappers.PatientMapper;
import owl.tree.rmfarma.patient.infrastructure.repository.PatientRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientPersistencePortAdapter implements PatientPersistencePort {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientResourceDto createPatient(PatientCreateResourceDto patientCreateResourceDto) {
        return patientMapper.toPatientResourceDto(patientRepository.save(patientMapper.toPatientEntity(patientCreateResourceDto)));
    }

    public PatientResourceDto findPatientByIdentification(String identification) {
        Patient patient = patientRepository.findByIdentification(identification);
        if (patient == null) return null;
        return patientMapper.toPatientResourceDto(patient);
    }

    public List<PatientResourceDto> findAll() {
        return this.patientRepository.findAll()
                .stream()
                .map(patientMapper::toPatientResourceDto)
                .toList();
    }

    @Override
    public List<PatientComboResourceDto> findPatientByIdentificationDebound(String identification) {
        return this.patientRepository.findPatientByIdentificationContaining(identification) ;
    }
}
