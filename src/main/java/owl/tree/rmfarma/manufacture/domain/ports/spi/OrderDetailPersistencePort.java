package owl.tree.rmfarma.manufacture.domain.ports.spi;

import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;

public interface OrderDetailPersistencePort {

    OrderDetailResourceDto create(OrderDetailCreateResourceDto orderDetailCreateResourceDto);
}
