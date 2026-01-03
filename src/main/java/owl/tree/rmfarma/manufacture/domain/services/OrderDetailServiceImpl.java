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
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.generateid.GeneratorIdResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.*;
import owl.tree.rmfarma.manufacture.domain.ports.api.OrderDetailServicePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.CommercialOrderDetailPersistencePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.GeneratorIdPersistencePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.OrderDetailPersistencePort;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.spi.CommercialProductPersistencePort;
import owl.tree.rmfarma.product.domain.ports.spi.ComplementPersistencePort;
import owl.tree.rmfarma.product.domain.ports.spi.ProductPersistencePort;
import owl.tree.rmfarma.shared.enumes.StateMachineOrderDetailsEnum;
import owl.tree.rmfarma.shared.exception.domain.IsEmptyException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailServicePort {

    private final OrderDetailPersistencePort orderDetailPersistencePort;
    private final CommercialOrderDetailPersistencePort commercialOrderDetailPersistencePort;
    private final CommercialProductPersistencePort commercialProductPersistencePort;
    private final ProductPersistencePort productPersistencePort;
    private final ViaPersistencePort viaPersistencePort;
    private final ComplementPersistencePort complementPersistencePort;
    private final GeneratorIdPersistencePort generatorIdPersistencePort;


    public List<CustomReportDTO> getCustomReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return this.orderDetailPersistencePort.getCustomReport(startDate, endDate);
    }

    public List<ResumeReportDto> getResumeReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return this.orderDetailPersistencePort.getResumeReport(startDate, endDate);
    }

    public List<ConcentrationReportDto> getConcentrationReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return this.orderDetailPersistencePort.getConcentrationReport(startDate, endDate);
    }

    @Override
    public List<PatientHistoryReportDto> patientHistoryReport() {
        return this.orderDetailPersistencePort.patientHistoryReport();
    }

    @Override
    @Transactional
    public OrderDetailResourceDto updateStatus(OrderDetailUpdateStatusResourceDto body) {
        log.info(body.toString());
        OrderDetailResourceDto orderDetailResourceDto = this.orderDetailPersistencePort.findByMasterRecord(body.getMasterRecord());
        if(orderDetailResourceDto == null) {
            throw new IllegalArgumentException("Order detail not found with master record: " + body.getMasterRecord());
        }
        String reason = body.getReasonForSuspension() != null ? body.getReasonForSuspension() : "";
        if((orderDetailResourceDto.getCommercialOrderDetails() == null || orderDetailResourceDto.getCommercialOrderDetails().isEmpty()) && body.getCode().equals("ACTIVE")) {
            throw new IsEmptyException("commercial", "Preparado");
        }
        if(body.getCode().equals("ACTIVE")) {
            reason = "";
        }
        this.orderDetailPersistencePort.updateStatus(orderDetailResourceDto.getId(), body.getCode(), reason);
        return orderDetailResourceDto;
    }

    @Override
    @Transactional
    public OrderDetailResourceDto updateOrderDetail(OrderDetailUpdateFormResourceDto body) {
        OrderDetailResourceDto orderDetailResourceDto = this.orderDetailPersistencePort.findByMasterRecord(body.getMasterRecord());
        OrderDetailResourceDto convert = this.convertToOrderDetailDto(body, orderDetailResourceDto);
        OrderDetailUpdateResourceDto orderUpdate =  this.generateOrderDetailUpdate(body, orderDetailResourceDto.getId());
        OrderDetailResourceDto response = this.orderDetailPersistencePort.updateOrderDetail(orderUpdate);
        this.taskCommercialDetail(convert);
        return response;
    }

    private OrderDetailResourceDto convertToOrderDetailDto (OrderDetailUpdateFormResourceDto body, OrderDetailResourceDto resource) {
        resource.setMasterRecord(body.getMasterRecord());
        List<CommercialOrderDetailResourceDto> commercialOrderDetailResourceDtos = body.getDetails().getCommercialPart().stream()
                .map(m -> CommercialOrderDetailResourceDto.builder()
                        .batch(m.getBatch())
                        .code(m.getCommercial())
                        .quantity(m.getPart())
                        .build())
                .collect(Collectors.toList());
        resource.setCommercialOrderDetails(commercialOrderDetailResourceDtos);
        return resource;
    }

    private void taskCommercialDetail (OrderDetailResourceDto resource) {
        this.commercialOrderDetailPersistencePort.deleteByOrderDetail(resource.getId());
        resource.getCommercialOrderDetails().forEach(f -> {
            CommercialAddCreateResourceUseCaseDto commercialAddCreateResourceUseCaseDto = CommercialAddCreateResourceUseCaseDto.builder()
                    .batch(f.getBatch())
                    .commercial(f.getCode())
                    .part(f.getQuantity())
                    .build();
            this.generateCommercialDetail(resource, commercialAddCreateResourceUseCaseDto);
        });
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

    public OrderDetailResourceDto createOrderDetail (MasterOrderCreateResourceUseCaseDto masterOrderCreateResourceUseCaseDto, String masterOrderId) {
        OrderDetailCreateResourceDto body = this.generateOrderDetail(masterOrderCreateResourceUseCaseDto, masterOrderId, this.generateID(LocalDate.now().getYear()));
        return this.orderDetailPersistencePort.create(body);
    }



    private OrderDetailUpdateResourceDto generateOrderDetailUpdate(OrderDetailUpdateFormResourceDto body, String orderId) {
        StateMachineOrderDetailsEnum state = StateMachineOrderDetailsEnum.ACTIVE;
        if(body.getDetails().getStatus() != null && !body.getDetails().getStatus().isEmpty()) {
            state = StateMachineOrderDetailsEnum.valueOf(body.getDetails().getStatus());
        }
        if(body.getDetails().getCommercialPart() == null || body.getDetails().getCommercialPart().isEmpty()) {
            state = StateMachineOrderDetailsEnum.PENDING;
        }
        if(state.equals(StateMachineOrderDetailsEnum.PENDING) && (body.getDetails().getCommercialPart() != null && !body.getDetails().getCommercialPart().isEmpty())){
            state = StateMachineOrderDetailsEnum.ACTIVE;

        }
        OrderDetailUpdateResourceDto detail = OrderDetailUpdateResourceDto.builder()
                .id(orderId)
                .prot(body.getDetails().getProt())
                .condition(body.getDetails().getCondition())
                .administrationTime(body.getDetails().getAdministrationTime())
                .quantity(body.getDetails().getDose())
                .administrationDate(body.getDetails().getAdministrationDate())
                .bedDay(body.getDetails().getBedDay())
                .volumeTotal(body.getDetails().getVolTotal())
                .unitMetric(body.getDetails().getUnitMetric())
                .expirationDate(body.getDetails().getExpirationDate())
                .productionDate(body.getDetails().getProductionDate())
                .observation(body.getDetails().getObservation())
                .status(state)
                .concentration(body.getDetails().getConcentration())
                .build();
        ProductResourceDto productResourceDto = this.productPersistencePort.findByCode(body.getDetails().getProductCode());
        if (productResourceDto != null) {
            detail.setProduct(productResourceDto.getId());
            detail.setProductCode(productResourceDto.getCode());
            detail.setProductName(productResourceDto.getDescription());
        }
        ComplementResourceDto complementResourceDto = this.complementPersistencePort.findByCode(body.getDetails().getComplementCode());
        if (complementResourceDto != null) {
            detail.setComplement(complementResourceDto.getId());
            detail.setComplementCode(complementResourceDto.getCode());
            detail.setComplementName(complementResourceDto.getDescription());
        }
        ViaResourceDto viaResourceDto = this.viaPersistencePort.findByCode(body.getDetails().getVia());
        if(viaResourceDto != null) {
            detail.setVia(viaResourceDto.getId());
            detail.setViaCode(viaResourceDto.getCode());
            detail.setViaDescription(viaResourceDto.getDescription());
        }
        detail.setMasterOrder(body.getMaster());
        detail.setMasterRecord(body.getMasterRecord());
        return detail;
    }

    private OrderDetailCreateResourceDto generateOrderDetail(MasterOrderCreateResourceUseCaseDto masterOrderCreateResourceUseCaseDto, String masterOrderId, String masterRecord) {
        StateMachineOrderDetailsEnum state = StateMachineOrderDetailsEnum.ACTIVE;
        if(masterOrderCreateResourceUseCaseDto.getDetails().getStatus() != null && !masterOrderCreateResourceUseCaseDto.getDetails().getStatus().isEmpty()) {
            state = StateMachineOrderDetailsEnum.valueOf(masterOrderCreateResourceUseCaseDto.getDetails().getStatus());
        }
        if(masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart() == null || masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart().isEmpty()) {
            state = StateMachineOrderDetailsEnum.PENDING;
        }

        if(state.equals(StateMachineOrderDetailsEnum.PENDING) && (masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart() != null && !masterOrderCreateResourceUseCaseDto.getDetails().getCommercialPart().isEmpty())){
            state = StateMachineOrderDetailsEnum.ACTIVE;

        }
        OrderDetailCreateResourceDto detail = OrderDetailCreateResourceDto.builder()
                .prot(masterOrderCreateResourceUseCaseDto.getDetails().getProt())
                .condition(masterOrderCreateResourceUseCaseDto.getDetails().getCondition())
                .administrationTime(masterOrderCreateResourceUseCaseDto.getDetails().getAdministrationTime())
                .quantity(masterOrderCreateResourceUseCaseDto.getDetails().getDose())
                .volumeTotal(masterOrderCreateResourceUseCaseDto.getDetails().getVolTotal())
                .unitMetric(masterOrderCreateResourceUseCaseDto.getDetails().getUnitMetric())
                .expirationDate(masterOrderCreateResourceUseCaseDto.getDetails().getExpirationDate())
                .administrationDate(masterOrderCreateResourceUseCaseDto.getDetails().getAdministrationDate())
                .bedDay(masterOrderCreateResourceUseCaseDto.getDetails().getBedDay())
                .productionDate(masterOrderCreateResourceUseCaseDto.getDetails().getProductionDate())
                .observation(masterOrderCreateResourceUseCaseDto.getDetails().getObservation())
                .concentration(masterOrderCreateResourceUseCaseDto.getDetails().getConcentration())
                .status(state)
                .build();
        ProductResourceDto productResourceDto = this.productPersistencePort.findByCode(masterOrderCreateResourceUseCaseDto.getDetails().getProductCode());
        if (productResourceDto != null) {
            detail.setProduct(productResourceDto.getId());
            detail.setProductCode(productResourceDto.getCode());
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

    private String generateID(int currentYear) {
        GeneratorIdResourceDto generatorIdResourceDto = this.generatorIdPersistencePort.generateId(currentYear);
        return generatorIdResourceDto.getCorrelative() + "/" + generatorIdResourceDto.getYear();
    }
}
