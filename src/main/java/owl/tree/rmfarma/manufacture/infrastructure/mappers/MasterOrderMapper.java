package owl.tree.rmfarma.manufacture.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;
import owl.tree.rmfarma.domain.infrastructure.entities.DocumentType;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;
import owl.tree.rmfarma.domain.infrastructure.mappers.DocumentTypeMapper;
import owl.tree.rmfarma.domain.infrastructure.mappers.DocumentTypeMapperImpl;
import owl.tree.rmfarma.domain.infrastructure.mappers.ViaMapper;
import owl.tree.rmfarma.domain.infrastructure.mappers.ViaMapperImpl;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.infrastructure.entities.DiagnosisOrderStage;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;
import owl.tree.rmfarma.patient.infrastructure.mappers.PatientMapper;
import owl.tree.rmfarma.patient.infrastructure.mappers.PatientMapperImpl;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MasterOrderMapper {

    PatientMapper patientMapper = new PatientMapperImpl();
    DocumentTypeMapper documentTypeMapper = new DocumentTypeMapperImpl();
    OrderDetailMapper orderDetailMapper = new OrderDetailMapperImpl();
    DiagnosisOrderStageMapper diagnosisOrderStageMapper = new DiagnosisOrderStageMapperImpl();

    default MasterOrderResourceDto toMasterResourceDto(MasterOrder entity) {
        if (entity == null) return null;
        MasterOrderResourceDto resource = new MasterOrderResourceDto();
        BeanUtils.copyProperties(entity, resource);
        resource.setState(entity.getStatus());
        if (entity.getPatient() != null) resource.setPatient(patientMapper.toPatientResourceDto(entity.getPatient()));
        if (entity.getDocumentType() != null)
            resource.setDocumentType(documentTypeMapper.toDocumentTypeResourceDto(entity.getDocumentType()));
        if(entity.getDiagnosisOrderStage() != null) resource.setDiagnosisOrderStage(diagnosisOrderStageMapper.toDiagnosisOrderResourceDto(entity.getDiagnosisOrderStage()));
        if (entity.getOrderDetails() != null && !entity.getOrderDetails().isEmpty()) {
            Set<OrderDetailResourceDto> orderDetailResourceDtos = new HashSet<>();
            entity.getOrderDetails().forEach(orderDetail -> orderDetailResourceDtos.add(orderDetailMapper.toOrderDetailResourceDto(orderDetail)));
            resource.setOrderDetails(orderDetailResourceDtos);
        }
        return resource;
    }

    default MasterOrder toMasterOrderEntity(MasterOrderCreateResourceDto resource) {
        if (resource == null) return null;
        MasterOrder entity = new MasterOrder();
        BeanUtils.copyProperties(resource, entity);
        if (resource.getPatient() != null && !resource.getPatient().isEmpty()) {
            entity.setPatient(Patient.builder().id(resource.getPatient()).build());
        }
        if (resource.getDocumentType() != null && !resource.getDocumentType().isEmpty()) {
            entity.setDocumentType(DocumentType.builder().id(resource.getDocumentType()).build());
        }
        if(resource.getDiagnosisOrderStage() != null) {
            entity.setDiagnosisOrderStage(DiagnosisOrderStage.builder().id(resource.getDiagnosisOrderStage()).build());
        }
        return entity;
    }
}
