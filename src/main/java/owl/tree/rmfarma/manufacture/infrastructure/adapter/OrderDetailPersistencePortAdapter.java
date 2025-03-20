package owl.tree.rmfarma.manufacture.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.*;
import owl.tree.rmfarma.manufacture.domain.ports.spi.OrderDetailPersistencePort;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;
import owl.tree.rmfarma.manufacture.infrastructure.mappers.OrderDetailMapper;
import owl.tree.rmfarma.manufacture.infrastructure.repository.OrderDetailRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderDetailPersistencePortAdapter implements OrderDetailPersistencePort {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailResourceDto create(OrderDetailCreateResourceDto orderDetailCreateResourceDto) {
        return this.orderDetailMapper.toOrderDetailResourceDto(this.orderDetailRepository.save(this.orderDetailMapper.toOrderDetailEntity(orderDetailCreateResourceDto)));
    }

    public List<CustomReportDTO> getCustomReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return this.orderDetailRepository.getCustomReport(startDate, endDate);
    }

    public List<ResumeReportDto> getResumeReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return this.orderDetailRepository.getResumeReport(startDate, endDate);
    }

    public OrderDetailResourceDto findByMasterRecord(String masterRecord) {
        OrderDetail entity = this.orderDetailRepository.findByMasterRecord(masterRecord).orElse(null);
        if(entity == null) {
            return null;
        }
        return this.orderDetailMapper.toOrderDetailResourceDto(entity);
    }

    @Override
    public OrderDetailResourceDto updateOrderDetail(OrderDetailUpdateResourceDto orderUpdate) {
        return this.orderDetailMapper.toOrderDetailResourceDto(this.orderDetailRepository.save(this.orderDetailMapper.toOrderDetailEntityUpdate(orderUpdate)));
    }

}
