package owl.tree.rmfarma.patient.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;
import owl.tree.rmfarma.domain.infrastructure.mappers.IsapreMapper;
import owl.tree.rmfarma.domain.infrastructure.mappers.IsapreMapperImpl;
import owl.tree.rmfarma.patient.domain.data.patient.PatientCreateResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    IsapreMapper isapreMapper = new IsapreMapperImpl();
    DiagnosisPatientMapper diagnosisPatientMapper = new DiagnosisPatientMapperImpl();

    default Patient toPatientEntity(PatientCreateResourceDto resource) {
        if (resource == null) {
            return null;
        }
        Patient patientEntity = Patient.builder()
                .rut(resource.getRut())
                .identification(resource.getIdentification())
                .name(resource.getName().toUpperCase())
                .type(resource.getType().toUpperCase())
                .lastName(resource.getLastName().toUpperCase())
                .isapre((resource.getIsapre() != null && !resource.getIsapre().isEmpty()) ? Isapre.builder().id(resource.getIsapre()).build() : null)
                .build();
        if (resource.getId() != null) patientEntity.setId(resource.getId());
        return patientEntity;
    }

    default PatientResourceDto toPatientResourceDto(Patient entity) {
        PatientResourceDto resource = PatientResourceDto.builder()
                .id(entity.getId())
                .rut(entity.getRut())
                .type(entity.getType())
                .identification(entity.getIdentification())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .build();
        if (entity.getIsapre() != null) resource.setIsapre(isapreMapper.toIsapreResourceDto(entity.getIsapre()));
        if (entity.getDiagnosisPatients() != null) resource.setDiagnosisPatient(entity.getDiagnosisPatients().stream()
                .map(diagnosisPatientMapper::toDiagnosisPatientResource)
                .collect(Collectors.toSet()));
        return resource;
    }
}
