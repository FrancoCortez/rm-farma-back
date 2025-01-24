package owl.tree.rmfarma.manufacture.domain.ports.spi;

import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailResourceDto;

public interface CommercialOrderDetailPersistencePort {
    CommercialOrderDetailResourceDto save(CommercialOrderDetailCreateResourceDto commercialOrderDetail);
}
