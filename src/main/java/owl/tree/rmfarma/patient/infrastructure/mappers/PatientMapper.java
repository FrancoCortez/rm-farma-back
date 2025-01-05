package owl.tree.rmfarma.patient.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import owl.tree.rmfarma.country.infrastructure.entities.City;
import owl.tree.rmfarma.country.infrastructure.mappers.CityMapper;
import owl.tree.rmfarma.country.infrastructure.mappers.CityMapperImpl;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;
import owl.tree.rmfarma.doctor.infrastructure.mappers.DoctorMapper;
import owl.tree.rmfarma.doctor.infrastructure.mappers.DoctorMapperImpl;
import owl.tree.rmfarma.domain.infrastructure.entities.Clinic;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;
import owl.tree.rmfarma.domain.infrastructure.entities.Schema;
import owl.tree.rmfarma.domain.infrastructure.mappers.*;
import owl.tree.rmfarma.patient.domain.data.patient.PatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapperImpl;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    CityMapper cityMapper = new CityMapperImpl();
    DoctorMapper doctorMapper = new DoctorMapperImpl();
    ServicesMapper servicesMapper = new ServicesMapperImpl();
    DiagnosisMapper diagnosisMapper = new DiagnosisMapperImpl();
    ClinicMapper clinicMapper = new ClinicMapperImpl();
    IsapreMapper isapreMapper = new IsapreMapperImpl();
    SchemaMapper schemaMapper = new SchemaMapperImpl();

    default Patient toPatientEntity (PatientCreateResourceDto resource){
        if(resource == null){
            return null;
        }
        return Patient.builder()
                .rut(resource.getRut())
                .identification(resource.getIdentification())
                .name(resource.getName())
                .lastName(resource.getLastName())
                .villa(resource.getVilla())
                .street(resource.getStreet())
                .houseNumber((resource.getHouseNumber() == null) ? null : resource.getHouseNumber().toString())
                //.houseNumber(resource.getHouseNumber().toString())
                .dateOfBirth(resource.getDateOfBirth())
                .phone(resource.getPhone())
                .email(resource.getEmail())
                .cycleNumber(resource.getCycleNumber())
                .cycleDay(resource.getCycleDay())
                .city((resource.getCity() != null && !resource.getCity().isEmpty()) ? City.builder().id(resource.getCity()).build() : null)
                .doctor((resource.getDoctor() != null && !resource.getDoctor().isEmpty()) ? Doctor.builder().id(resource.getDoctor()).build() : null)
                .services((resource.getServices() != null && !resource.getServices().isEmpty()) ? Services.builder().id(resource.getServices()).build() : null)
                .diagnosis((resource.getDiagnosis() != null && !resource.getDiagnosis().isEmpty()) ? Diagnosis.builder().id(resource.getDiagnosis()).build() : null)
                .clinic((resource.getClinic() != null && !resource.getClinic().isEmpty()) ? Clinic.builder().id(resource.getClinic()).build() : null)
                .isapre((resource.getIsapre() != null && !resource.getIsapre().isEmpty()) ? Isapre.builder().id(resource.getIsapre()).build() : null)
                .schema((resource.getSchema() != null && !resource.getSchema().isEmpty()) ? Schema.builder().id(resource.getSchema()).build() : null)
                .build();
    }

    default PatientResourceDto toPatientResourceDto(Patient patient) {
        PatientResourceDto patientResourceDto = PatientResourceDto.builder()
                .id(patient.getId())
                .rut(patient.getRut())
                .identification(patient.getIdentification())
                .name(patient.getName())
                .lastName(patient.getLastName())
                .villa(patient.getVilla())
                .street(patient.getStreet())
                .houseNumber(patient.getHouseNumber())
                .dateOfBirth(patient.getDateOfBirth())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .cycleNumber(patient.getCycleNumber())
                .cycleDay(patient.getCycleDay())
                .build();
        if(patient.getCity() != null) patientResourceDto.setCity(cityMapper.toCityResourceDto(patient.getCity()));
        if(patient.getDoctor() != null) patientResourceDto.setDoctor(doctorMapper.toDoctorResourceDto(patient.getDoctor()));
        if(patient.getServices() != null) patientResourceDto.setServices(servicesMapper.toServiceResourceDto(patient.getServices()));
        if(patient.getDiagnosis() != null) patientResourceDto.setDiagnosis(diagnosisMapper.toDiagnosisResourceDto(patient.getDiagnosis()));
        if(patient.getClinic() != null) patientResourceDto.setClinic(clinicMapper.toClinicResourceDto(patient.getClinic()));
        if(patient.getIsapre() != null) patientResourceDto.setIsapre(isapreMapper.toIsapreResourceDto(patient.getIsapre()));
        if(patient.getSchema() != null) patientResourceDto.setSchema(schemaMapper.toSchemaResourceDto(patient.getSchema()));
        return patientResourceDto;
    }
}
