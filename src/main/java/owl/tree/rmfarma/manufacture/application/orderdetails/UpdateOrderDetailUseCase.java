package owl.tree.rmfarma.manufacture.application.orderdetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailUpdateFormResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.api.OrderDetailServicePort;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateOrderDetailUseCase {
    private final OrderDetailServicePort orderDetailServicePort;


    public OrderDetailResourceDto updateOrderDetail(OrderDetailUpdateFormResourceDto body) {
        return this.orderDetailServicePort.updateOrderDetail(body);
    }
}
