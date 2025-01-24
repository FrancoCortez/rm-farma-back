package owl.tree.rmfarma.manufacture.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;
import owl.tree.rmfarma.manufacture.infrastructure.entities.DiagnosisOrderStage;
import owl.tree.rmfarma.patient.infrastructure.entities.DiagnosisPatient;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;
import owl.tree.rmfarma.patient.infrastructure.mappers.DiagnosisPatientMapper;
import owl.tree.rmfarma.patient.infrastructure.mappers.DiagnosisPatientMapperImpl;
import owl.tree.rmfarma.patient.infrastructure.mappers.PatientMapper;
import owl.tree.rmfarma.patient.infrastructure.mappers.PatientMapperImpl;

@Mapper(componentModel = "spring")
public interface DiagnosisOrderStageMapper {

    DiagnosisPatientMapper diagnosisPatientMapper = new DiagnosisPatientMapperImpl();
    PatientMapper patientMapper = new PatientMapperImpl();

    default DiagnosisOrderStage toDiagnosisOrderEntity (DiagnosisOrderStageCreateResourceDto resource) {
        DiagnosisOrderStage entity = new DiagnosisOrderStage();
        entity.setPatient(Patient.builder().id(resource.getPatient()).build());
        entity.setDiagnosisPatient(DiagnosisPatient.builder().id(resource.getDiagnosisPatient()).build());
        entity.setIdentificationPatient(resource.getPatientIdentification());
        entity.setStatus(resource.getStatus());
        entity.setCreatedAt(resource.getCreatedAt());
        entity.setUpdatedAt(resource.getUpdatedAt());
        entity.setCycleDay(resource.getCycleDay());
        entity.setCycleNumber(resource.getCycleNumber());
        entity.setProductionDate(resource.getProductionDate());
        return entity;
    }

    default DiagnosisOrderStageResourceDto toDiagnosisOrderResourceDto(DiagnosisOrderStage entity) {
        DiagnosisOrderStageResourceDto.DiagnosisOrderStageResourceDtoBuilder diagnosisOrderStageResourceDto = DiagnosisOrderStageResourceDto.builder();
        diagnosisOrderStageResourceDto.id(entity.getId());
        diagnosisOrderStageResourceDto.identificationPatient(entity.getIdentificationPatient());
        diagnosisOrderStageResourceDto.cycleNumber(entity.getCycleNumber());
        diagnosisOrderStageResourceDto.cycleDay(entity.getCycleDay());
        diagnosisOrderStageResourceDto.productionDate(entity.getProductionDate());
        diagnosisOrderStageResourceDto.createdAt(entity.getCreatedAt());
        diagnosisOrderStageResourceDto.updatedAt(entity.getUpdatedAt());
        diagnosisOrderStageResourceDto.status(entity.getStatus());
        if(entity.getDiagnosisPatient() != null) {
            diagnosisOrderStageResourceDto.diagnosisPatient(diagnosisPatientMapper.toDiagnosisPatientResource(entity.getDiagnosisPatient()));
        }
        if(entity.getPatient() != null) {
            diagnosisOrderStageResourceDto.patient(patientMapper.toPatientResourceDto(entity.getPatient()));
        }
        return diagnosisOrderStageResourceDto.build();
    }
}
