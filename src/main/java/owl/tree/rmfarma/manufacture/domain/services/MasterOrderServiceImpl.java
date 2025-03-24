package owl.tree.rmfarma.manufacture.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import owl.tree.rmfarma.manufacture.application.masterorder.data.CommercialAddCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.application.masterorder.data.MasterOrderCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.InternalMasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.api.MasterOrderServicePort;
import owl.tree.rmfarma.manufacture.domain.ports.api.OrderDetailServicePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.CommercialOrderDetailPersistencePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.MasterOrderPersistencePort;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.spi.PatientPersistencePort;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.spi.CommercialProductPersistencePort;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterOrderServiceImpl implements MasterOrderServicePort {

    private final MasterOrderPersistencePort masterOrderPersistencePort;
    private final PatientPersistencePort patientPersistencePort;
    private final OrderDetailServicePort orderDetailServicePort;
    private final CommercialProductPersistencePort commercialProductPersistencePort;
    private final CommercialOrderDetailPersistencePort commercialOrderDetailPersistencePort;

    @Transactional
    public List<MasterOrderResourceDto> findAll(OffsetDateTime searchDay, String searchIdentification) {
        return this.masterOrderPersistencePort.findAll(searchDay, searchIdentification);
    }

    @Transactional
    public MasterOrderResourceDto create(MasterOrderCreateResourceUseCaseDto masterOrderCreateResourceUseCaseDto) {
        MasterOrderResourceDto resourceDto = this.masterOrderPersistencePort.findById(masterOrderCreateResourceUseCaseDto.getMaster());
        OrderDetailResourceDto responseOrderDetail = this.orderDetailServicePort.createOrderDetail(masterOrderCreateResourceUseCaseDto, resourceDto.getId());
        if (masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart() != null && !masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart().isEmpty()) {
            masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart().forEach(commercialAdd -> this.generateCommercialDetail(responseOrderDetail, commercialAdd));
        }
        return resourceDto;
    }

    public MasterOrderResourceDto createInternalProcess(InternalMasterOrderCreateResourceDto masterOrderCreateResourceDto, DiagnosisOrderStageResourceDto response) {
        MasterOrderCreateResourceDto resource = this.generateInternalMasterOrder(masterOrderCreateResourceDto, response);
        MasterOrderResourceDto masterOrderResourceDto = this.masterOrderPersistencePort.create(resource);
        return this.masterOrderPersistencePort.findById(masterOrderResourceDto.getId());
    }

    private MasterOrderCreateResourceDto generateInternalMasterOrder(InternalMasterOrderCreateResourceDto masterOrderCreateResourceUseCaseDto, DiagnosisOrderStageResourceDto response) {
        PatientResourceDto patientResourceDto = this.patientPersistencePort.findPatientByIdentification(masterOrderCreateResourceUseCaseDto.getPatientIdentification());
        MasterOrderCreateResourceDto create =  MasterOrderCreateResourceDto.builder()
                .diagnosisOrderStage(response.getId())
                .patient(patientResourceDto.getId())
                .productionDate(masterOrderCreateResourceUseCaseDto.getProductionDate())
                .patientName(patientResourceDto.getName())
                .patientLastName(patientResourceDto.getLastName())
                .patientRut(patientResourceDto.getRut())
                .patientIdentification(patientResourceDto.getIdentification())
//                .isapreCode(patientResourceDto.getIsapre().getCode())
//                .isapreName(patientResourceDto.getIsapre().getDescription())
                .doctorName(response.getDiagnosisPatient().getDoctor().getName())
                .doctorRut(response.getDiagnosisPatient().getDoctor().getRut())
                .diagnosisCode(response.getDiagnosisPatient().getDiagnosis().getCode())
                .diagnosisName(response.getDiagnosisPatient().getDiagnosis().getDescription())
                .cycleNumber(response.getCycleNumber())
                .cycleDay(response.getCycleDay())
                .schemaCode(response.getDiagnosisPatient().getSchema().getCode())
                .schemaName(response.getDiagnosisPatient().getSchema().getDescription())
                .unitHospitalCode(response.getDiagnosisPatient().getHospitalUnit().getCode())
                .unitHospitalName(response.getDiagnosisPatient().getHospitalUnit().getDescription())
                .status(response.getStatus())
                .build();
        if(patientResourceDto.getIsapre() != null && patientResourceDto.getIsapre().getCode() != null){
            create.setIsapreCode(patientResourceDto.getIsapre().getCode());
            create.setIsapreName(patientResourceDto.getIsapre().getDescription());
        }
        return create;
    }


    private void generateCommercialDetail(OrderDetailResourceDto orderDetailResourceDto, CommercialAddCreateResourceUseCaseDto commercialAdd) {
        CommercialOrderDetailCreateResourceDto commercialOrderDetail = CommercialOrderDetailCreateResourceDto.builder()
                .quantity(commercialAdd.getPart())
                .batch(commercialAdd.getBatch())
                .orderDetail(orderDetailResourceDto.getId())
                .build();
        CommercialProductResourceDto commercialResponse = this.commercialProductPersistencePort.findByCode(commercialAdd.getCommercial());
        if (commercialResponse != null) {
            commercialOrderDetail.setCommercial(commercialResponse.getDescription());
            commercialOrderDetail.setCode(commercialResponse.getCode());
            commercialOrderDetail.setLaboratory(commercialResponse.getLaboratory());
            commercialOrderDetail.setCommercialProduct(commercialResponse.getId());
        }
        this.commercialOrderDetailPersistencePort.save(commercialOrderDetail);
    }
}
