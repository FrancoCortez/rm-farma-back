package owl.tree.rmfarma.manufacture.domain.ports.spi;

import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;

public interface CommercialOrderDetailPersistencePort {
    CommercialOrderDetailResourceDto save(CommercialOrderDetailCreateResourceDto commercialOrderDetail);

    void deleteByOrderDetail(String orderDetail);
}
