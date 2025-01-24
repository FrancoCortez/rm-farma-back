package owl.tree.rmfarma.manufacture.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.ViaPersistencePort;
import owl.tree.rmfarma.manufacture.application.masterorder.data.CommercialAddCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.application.masterorder.data.MasterOrderCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.generateid.GeneratorIdResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.InternalMasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.api.MasterOrderServicePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.CommercialOrderDetailPersistencePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.GeneratorIdPersistencePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.MasterOrderPersistencePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.OrderDetailPersistencePort;
import owl.tree.rmfarma.manufacture.infrastructure.entities.CommercialOrderDetail;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.patient.domain.ports.spi.PatientPersistencePort;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.spi.CommercialProductPersistencePort;
import owl.tree.rmfarma.product.domain.ports.spi.ComplementPersistencePort;
import owl.tree.rmfarma.product.domain.ports.spi.ProductPersistencePort;
import owl.tree.rmfarma.product.infrastructure.entities.CommercialProduct;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterOrderServiceImpl implements MasterOrderServicePort {

    private final MasterOrderPersistencePort masterOrderPersistencePort;
    private final OrderDetailPersistencePort orderDetailPersistencePort;
    private final GeneratorIdPersistencePort generatorIdPersistencePort;
    private final PatientPersistencePort patientPersistencePort;
    private final ViaPersistencePort viaPersistencePort;
    private final ProductPersistencePort productPersistencePort;
    private final ComplementPersistencePort complementPersistencePort;
    private final CommercialProductPersistencePort commercialProductPersistencePort;
    private final CommercialOrderDetailPersistencePort commercialOrderDetailPersistencePort;

    public List<MasterOrderResourceDto> findAll(OffsetDateTime searchDay, String searchIdentification) {
        return this.masterOrderPersistencePort.findAll(searchDay, searchIdentification);
    }

    @Transactional
    public MasterOrderResourceDto create(MasterOrderCreateResourceUseCaseDto masterOrderCreateResourceUseCaseDto) {
        MasterOrderResourceDto resourceDto = this.masterOrderPersistencePort.findById(masterOrderCreateResourceUseCaseDto.getMaster());
        OrderDetailCreateResourceDto orderDetailCreateResourceDto = this.generateOrderDetail(masterOrderCreateResourceUseCaseDto, resourceDto.getId(), this.generateID(LocalDate.now().getYear()));
        OrderDetailResourceDto responseOrderDetail = this.orderDetailPersistencePort.create(orderDetailCreateResourceDto);
        if(masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart() != null && !masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart().isEmpty()) {
            masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart().forEach(commercialAdd -> this.generateCommercialDetail(responseOrderDetail, commercialAdd));
        }
        return resourceDto;
    }

    public MasterOrderResourceDto createInternalProcess (InternalMasterOrderCreateResourceDto masterOrderCreateResourceDto, DiagnosisOrderStageResourceDto response) {
        MasterOrderCreateResourceDto resource = this.generateInternalMasterOrder(masterOrderCreateResourceDto, response);
        MasterOrderResourceDto masterOrderResourceDto = this.masterOrderPersistencePort.create(resource);
        return this.masterOrderPersistencePort.findById(masterOrderResourceDto.getId());
    }

    private MasterOrderCreateResourceDto generateInternalMasterOrder(InternalMasterOrderCreateResourceDto masterOrderCreateResourceUseCaseDto, DiagnosisOrderStageResourceDto response) {
        PatientResourceDto patientResourceDto = this.patientPersistencePort.findPatientByIdentification(masterOrderCreateResourceUseCaseDto.getPatientIdentification());
        return MasterOrderCreateResourceDto.builder()
                .diagnosisOrderStage(response.getId())
                .patient(patientResourceDto.getId())
                .productionDate(masterOrderCreateResourceUseCaseDto.getProductionDate())
                .patientName(patientResourceDto.getName())
                .patientLastName(patientResourceDto.getLastName())
                .patientRut(patientResourceDto.getRut())
                .patientIdentification(patientResourceDto.getIdentification())
                .isapreCode(patientResourceDto.getIsapre().getCode())
                .isapreName(patientResourceDto.getIsapre().getDescription())
                .doctorName(response.getDiagnosisPatient().getDoctor().getName())
                .doctorRut(response.getDiagnosisPatient().getDoctor().getRut())
                .clinicCode(response.getDiagnosisPatient().getClinic().getCode())
                .clinicName(response.getDiagnosisPatient().getClinic().getDescription())
                .diagnosisCode(response.getDiagnosisPatient().getDiagnosis().getCode())
                .diagnosisName(response.getDiagnosisPatient().getDiagnosis().getDescription())
                .cycleNumber(response.getCycleNumber())
                .cycleDay(response.getCycleDay())
                .schemaCode(response.getDiagnosisPatient().getSchema().getCode())
                .schemaName(response.getDiagnosisPatient().getSchema().getDescription())
                .status(response.getStatus())
                .build();
    }

    private OrderDetailCreateResourceDto generateOrderDetail(MasterOrderCreateResourceUseCaseDto masterOrderCreateResourceUseCaseDto, String masterOrderId, String masterRecord) {

        OrderDetailCreateResourceDto detail = OrderDetailCreateResourceDto.builder()
                .prot(masterOrderCreateResourceUseCaseDto.getDetails().getProt())
                .condition(masterOrderCreateResourceUseCaseDto.getDetails().getCondition())
                .administrationTime(masterOrderCreateResourceUseCaseDto.getDetails().getAdministrationTime())
                .quantity(masterOrderCreateResourceUseCaseDto.getDetails().getDose())
                .volumeTotal(masterOrderCreateResourceUseCaseDto.getDetails().getVolTotal())
                .unitMetric(masterOrderCreateResourceUseCaseDto.getDetails().getUnitMetric())
                .expirationDate(masterOrderCreateResourceUseCaseDto.getDetails().getExpirationDate())
                .productionDate(masterOrderCreateResourceUseCaseDto.getDetails().getProductionDate())
                .observation(masterOrderCreateResourceUseCaseDto.getDetails().getObservation())
                .build();
        ProductResourceDto productResourceDto = this.productPersistencePort.findByCode(masterOrderCreateResourceUseCaseDto.getDetails().getProductCode());
        if (productResourceDto != null) {
            detail.setProduct(productResourceDto.getId());
            detail.setProductCode(productResourceDto.getCode());
            detail.setProductLaboratory(productResourceDto.getLaboratory());
            detail.setProductName(productResourceDto.getDescription());
        }
        ComplementResourceDto complementResourceDto = this.complementPersistencePort.findByCode(masterOrderCreateResourceUseCaseDto.getDetails().getComplementCode());
        if (complementResourceDto != null) {
            detail.setComplement(complementResourceDto.getId());
            detail.setComplementCode(complementResourceDto.getCode());
            detail.setComplementName(complementResourceDto.getDescription());
        }
        ViaResourceDto viaResourceDto = this.viaPersistencePort.findByCode(masterOrderCreateResourceUseCaseDto.getVia());
        if(viaResourceDto != null) {
            detail.setVia(viaResourceDto.getId());
            detail.setViaCode(viaResourceDto.getCode());
            detail.setViaDescription(viaResourceDto.getDescription());
        }
        detail.setMasterOrder(masterOrderId);
        detail.setMasterRecord(masterRecord);
        return detail;
    }

    private void generateCommercialDetail (OrderDetailResourceDto orderDetailResourceDto, CommercialAddCreateResourceUseCaseDto commercialAdd) {
        CommercialOrderDetailCreateResourceDto commercialOrderDetail = CommercialOrderDetailCreateResourceDto.builder()
                .quantity(commercialAdd.getPart())
                .batch(commercialAdd.getBatch())
                .orderDetail(orderDetailResourceDto.getId())
                .build();
        CommercialProductResourceDto commercialResponse = this.commercialProductPersistencePort.findByCode(commercialAdd.getCommercial());
        if(commercialResponse != null) {
            commercialOrderDetail.setCommercial(commercialResponse.getDescription());
            commercialOrderDetail.setCode(commercialResponse.getCode());
            commercialOrderDetail.setLaboratory(commercialResponse.getLaboratory());
            commercialOrderDetail.setCommercialProduct(commercialResponse.getId());
        }
        this.commercialOrderDetailPersistencePort.save(commercialOrderDetail);
    }

    private String generateID(int currentYear) {
        GeneratorIdResourceDto generatorIdResourceDto = this.generatorIdPersistencePort.generateId(currentYear);
        return generatorIdResourceDto.getCorrelative() + "/" + generatorIdResourceDto.getYear();
    }
}
