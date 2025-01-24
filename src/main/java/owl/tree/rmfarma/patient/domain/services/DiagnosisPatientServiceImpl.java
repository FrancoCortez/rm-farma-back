package owl.tree.rmfarma.patient.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.spi.DoctorPersistencePort;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.ClinicPersistencePort;
import owl.tree.rmfarma.domain.domain.ports.spi.DiagnosisPersistencePort;
import owl.tree.rmfarma.domain.domain.ports.spi.HospitalUnitPersistencePort;
import owl.tree.rmfarma.domain.domain.ports.spi.SchemaPersistencePort;
import owl.tree.rmfarma.patient.application.diagnosispatient.data.DiagnosisPatientCreateResourceUseCaseDto;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.api.DiagnosisPatientServicePort;
import owl.tree.rmfarma.patient.domain.ports.spi.DiagnosisPatientPersistencePort;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.IsEmptyException;

@Service
@RequiredArgsConstructor
public class DiagnosisPatientServiceImpl implements DiagnosisPatientServicePort {
    private final DiagnosisPatientPersistencePort diagnosisPatientPersistencePort;

    private final DoctorPersistencePort doctorPersistencePort;
    private final ClinicPersistencePort clinicPersistencePort;
    private final SchemaPersistencePort schemaPersistencePort;
    private final DiagnosisPersistencePort diagnosisPersistencePort;
    private final ServicesPersistencePort servicesPersistencePort;
    private final HospitalUnitPersistencePort hospitalUnitPersistencePort;

    public DiagnosisPatientResourceDto createDiagnosisPatient(DiagnosisPatientCreateResourceUseCaseDto entry, PatientResourceDto patient) {
        if (patient.getIdentification() == null || patient.getIdentification().isEmpty())
            throw new IsEmptyException("Identification", "Patient");
        DiagnosisPatientCreateResourceDto resource = DiagnosisPatientCreateResourceDto.builder()
                .identificationPatient(patient.getIdentification())
                .patientId(patient.getId())
                .cycleNumber(entry.getCycleNumber())
                .cycleDay(entry.getCycleDay())
                .build();
        DoctorResourceDto doctorResource = doctorPersistencePort.findById(entry.getDoctor());
        if (doctorResource != null) resource.setDoctor(doctorResource.getId());
        ClinicResourceDto clinicResource = clinicPersistencePort.findByCode(entry.getClinic());
        if (clinicResource != null) resource.setClinic(clinicResource.getId());
        SchemaResourceDto schemaResource = schemaPersistencePort.findByCode(entry.getSchema());
        if (schemaResource != null) resource.setSchema(schemaResource.getId());
        DiagnosisResourceDto diagnosisResource = diagnosisPersistencePort.findByCode(entry.getDiagnosis());
        if (diagnosisResource != null) resource.setDiagnosis(diagnosisResource.getId());
        ServiceResourceDto serviceResource = servicesPersistencePort.findByCode(entry.getServices());
        if (serviceResource != null) resource.setServices(serviceResource.getId());
        HospitalUnitResourceDto hospitalUnitResource = hospitalUnitPersistencePort.findByCode(entry.getHospitalUnit());
        if (hospitalUnitResource != null) resource.setHospitalUnit(hospitalUnitResource.getId());
        return diagnosisPatientPersistencePort.createDiagnosisPatient(resource);
    }
}
