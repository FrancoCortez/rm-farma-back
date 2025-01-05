package owl.tree.rmfarma.patient.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.country.domain.data.city.CityResourceDto;
import owl.tree.rmfarma.country.domain.ports.spi.CityPersistencePort;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.spi.DoctorPersistencePort;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.ClinicPersistencePort;
import owl.tree.rmfarma.domain.domain.ports.spi.DiagnosisPersistencePort;
import owl.tree.rmfarma.domain.domain.ports.spi.IsaprePersistencePort;
import owl.tree.rmfarma.domain.domain.ports.spi.SchemaPersistencePort;
import owl.tree.rmfarma.patient.application.patient.data.PatientCreateResourceUseCaseDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.api.PatientServicePort;
import owl.tree.rmfarma.patient.domain.ports.spi.PatientPersistencePort;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.IsEmptyException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientServicePort {

    private final PatientPersistencePort patientPersistencePort;
    private final CityPersistencePort cityPersistencePort;
    private final DoctorPersistencePort doctorPersistencePort;
    private final ClinicPersistencePort clinicPersistencePort;
    private final SchemaPersistencePort schemaPersistencePort;
    private final IsaprePersistencePort isaprePersistencePort;
    private final DiagnosisPersistencePort diagnosisPersistencePort;
    private final ServicesPersistencePort servicesPersistencePort;

    public PatientResourceDto createPatient(PatientCreateResourceUseCaseDto patientCreateResourceUseCaseDto){

        PatientCreateResourceDto patientCreateResourceDto = PatientCreateResourceDto.builder()
                .rut(patientCreateResourceUseCaseDto.getRut())
                .identification(patientCreateResourceUseCaseDto.getIdentification())
                .name(patientCreateResourceUseCaseDto.getName())
                .lastName(patientCreateResourceUseCaseDto.getLastName())
                .villa(patientCreateResourceUseCaseDto.getVilla())
                .street(patientCreateResourceUseCaseDto.getStreet())
                .houseNumber(patientCreateResourceUseCaseDto.getHouseNumber())
                .dateOfBirth(patientCreateResourceUseCaseDto.getDateOfBirth())
                .phone(patientCreateResourceUseCaseDto.getPhone())
                .email(patientCreateResourceUseCaseDto.getEmail())
                .cycleNumber(patientCreateResourceUseCaseDto.getCycleNumber())
                .cycleDay(patientCreateResourceUseCaseDto.getCycleDay())
                .build();
        DoctorResourceDto doctorResourceDto = this.doctorPersistencePort.findByRut(patientCreateResourceUseCaseDto.getDoctorRut());
        if(doctorResourceDto != null) patientCreateResourceDto.setDoctor(doctorResourceDto.getId());
        CityResourceDto cityResourceDto = cityPersistencePort.findById(patientCreateResourceUseCaseDto.getCity());
        if (cityResourceDto != null) patientCreateResourceDto.setCity(cityResourceDto.getId());
        ClinicResourceDto clinicResourceDto = this.clinicPersistencePort.findByCode(patientCreateResourceUseCaseDto.getClinic());
        if (clinicResourceDto != null) patientCreateResourceDto.setClinic(clinicResourceDto.getId());
        SchemaResourceDto schemaResourceDto = this.schemaPersistencePort.findByCode(patientCreateResourceUseCaseDto.getSchema());
        if (schemaResourceDto != null) patientCreateResourceDto.setSchema(schemaResourceDto.getId());
        IsapreResourceDto isapreResourceDto = this.isaprePersistencePort.findByCode(patientCreateResourceUseCaseDto.getIsapre());
        if (isapreResourceDto != null) patientCreateResourceDto.setIsapre(isapreResourceDto.getId());
        DiagnosisResourceDto diagnosisResourceDto = this.diagnosisPersistencePort.findByCode(patientCreateResourceUseCaseDto.getDiagnosis());
        if (diagnosisResourceDto != null) patientCreateResourceDto.setDiagnosis(diagnosisResourceDto.getId());
        ServiceResourceDto servicesResourceDto = this.servicesPersistencePort.findByCode(patientCreateResourceUseCaseDto.getServices());
        if (servicesResourceDto != null) patientCreateResourceDto.setServices(servicesResourceDto.getId());
        return this.patientPersistencePort.createPatient(patientCreateResourceDto);
    }

    @Override
    public PatientResourceDto findPatientByIdentification(String identification) {
        if(identification == null || identification.isEmpty()) throw new IsEmptyException("Identification", "Patient");
        PatientResourceDto result =  this.patientPersistencePort.findPatientByIdentification(identification);
        if(result == null) throw new NotFoundException("Identification", "Patient");
        return result;
    }
}
