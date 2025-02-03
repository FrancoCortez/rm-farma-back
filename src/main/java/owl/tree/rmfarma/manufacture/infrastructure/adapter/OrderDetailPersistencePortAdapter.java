package owl.tree.rmfarma.manufacture.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.spi.OrderDetailPersistencePort;
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
}
