package owl.tree.rmfarma.manufacture.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.spi.OrderDetailPersistencePort;
import owl.tree.rmfarma.manufacture.infrastructure.mappers.OrderDetailMapper;
import owl.tree.rmfarma.manufacture.infrastructure.repository.OrderDetailRepository;

@Component
@RequiredArgsConstructor
public class OrderDetailPersistencePortAdapter implements OrderDetailPersistencePort {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailResourceDto create(OrderDetailCreateResourceDto orderDetailCreateResourceDto) {
        return this.orderDetailMapper.toOrderDetailResourceDto(this.orderDetailRepository.save(this.orderDetailMapper.toOrderDetailEntity(orderDetailCreateResourceDto)));
    }
}
