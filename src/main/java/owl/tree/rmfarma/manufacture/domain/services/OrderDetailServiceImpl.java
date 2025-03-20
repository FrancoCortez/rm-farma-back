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
        OrderDetailUpdateResourceDto detail = OrderDetailUpdateResourceDto.builder()
                .id(orderId)
                .prot(body.getDetails().getProt())
                .condition(body.getDetails().getCondition())
                .administrationTime(body.getDetails().getAdministrationTime())
                .quantity(body.getDetails().getDose())
                .volumeTotal(body.getDetails().getVolTotal())
                .unitMetric(body.getDetails().getUnitMetric())
                .expirationDate(body.getDetails().getExpirationDate())
                .productionDate(body.getDetails().getProductionDate())
                .observation(body.getDetails().getObservation())
                .build();
        ProductResourceDto productResourceDto = this.productPersistencePort.findByCode(body.getDetails().getProductCode());
        if (productResourceDto != null) {
            detail.setProduct(productResourceDto.getId());
            detail.setProductCode(productResourceDto.getCode());
            detail.setProductLaboratory(productResourceDto.getLaboratory());
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

    private String generateID(int currentYear) {
        GeneratorIdResourceDto generatorIdResourceDto = this.generatorIdPersistencePort.generateId(currentYear);
        return generatorIdResourceDto.getCorrelative() + "/" + generatorIdResourceDto.getYear();
    }
}
