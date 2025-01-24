package owl.tree.rmfarma.patient.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;
import owl.tree.rmfarma.doctor.infrastructure.mappers.DoctorMapper;
import owl.tree.rmfarma.doctor.infrastructure.mappers.DoctorMapperImpl;
import owl.tree.rmfarma.domain.infrastructure.entities.Clinic;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;
import owl.tree.rmfarma.domain.infrastructure.entities.HospitalUnit;
import owl.tree.rmfarma.domain.infrastructure.entities.Schema;
import owl.tree.rmfarma.domain.infrastructure.mappers.*;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.diagnosispatient.DiagnosisPatientResourceDto;
import owl.tree.rmfarma.patient.infrastructure.entities.DiagnosisPatient;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapperImpl;

@Mapper(componentModel = "spring")
public interface DiagnosisPatientMapper {

    DoctorMapper doctorMapper = new DoctorMapperImpl();
    ServicesMapper servicesMapper = new ServicesMapperImpl();
    DiagnosisMapper diagnosisMapper = new DiagnosisMapperImpl();
    ClinicMapper clinicMapper = new ClinicMapperImpl();
    SchemaMapper schemaMapper = new SchemaMapperImpl();
    HospitalUnitMapper hospitalUnitMapper = new HospitalUnitMapperImpl();

    default DiagnosisPatient toDiagnosisPatientEntity(DiagnosisPatientCreateResourceDto resource) {
        DiagnosisPatient entity = DiagnosisPatient.builder()
                .identificationPatient(resource.getIdentificationPatient())
                .patient(Patient.builder().id(resource.getPatientId()).build())
                .cycleNumber(resource.getCycleNumber())
                .cycleDay(resource.getCycleDay())
                .doctor((resource.getDoctor() != null && !resource.getDoctor().isEmpty()) ? Doctor.builder().id(resource.getDoctor()).build() : null)
                .services((resource.getServices() != null && !resource.getServices().isEmpty()) ? Services.builder().id(resource.getServices()).build() : null)
                .diagnosis((resource.getDiagnosis() != null && !resource.getDiagnosis().isEmpty()) ? Diagnosis.builder().id(resource.getDiagnosis()).build() : null)
                .clinic((resource.getClinic() != null && !resource.getClinic().isEmpty()) ? Clinic.builder().id(resource.getClinic()).build() : null)
                .schema((resource.getSchema() != null && !resource.getSchema().isEmpty()) ? Schema.builder().id(resource.getSchema()).build() : null)
                .hospitalUnit((resource.getHospitalUnit() != null && !resource.getHospitalUnit().isEmpty()) ? HospitalUnit.builder().id(resource.getHospitalUnit()).build() : null)
                .build();
        if (resource.getId() != null) entity.setId(resource.getId());
        return entity;
    }

    default DiagnosisPatientResourceDto toDiagnosisPatientResource(DiagnosisPatient entity) {
        DiagnosisPatientResourceDto resource = DiagnosisPatientResourceDto.builder()
                .id(entity.getId())
                .identificationPatient(entity.getIdentificationPatient())
                .cycleNumber(entity.getCycleNumber())
                .cycleDay(entity.getCycleDay())
                .build();
        if (entity.getDoctor() != null) resource.setDoctor(doctorMapper.toDoctorResourceDto(entity.getDoctor()));
        if (entity.getSchema() != null) resource.setSchema(schemaMapper.toSchemaResourceDto(entity.getSchema()));
        if (entity.getServices() != null)
            resource.setServices(servicesMapper.toServiceResourceDto(entity.getServices()));
        if (entity.getDiagnosis() != null)
            resource.setDiagnosis(diagnosisMapper.toDiagnosisResourceDto(entity.getDiagnosis()));
        if (entity.getClinic() != null) resource.setClinic(clinicMapper.toClinicResourceDto(entity.getClinic()));
        if (entity.getHospitalUnit() != null)
            resource.setHospitalUnit(hospitalUnitMapper.toHospitalUnitResourceDto(entity.getHospitalUnit()));
        return resource;
    }

}
