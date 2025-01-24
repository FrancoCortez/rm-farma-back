package owl.tree.rmfarma.product.domain.ports.spi;

import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;

import java.util.List;

public interface CommercialProductPersistencePort {
    List<CommercialProductResourceDto> findAll();

    CommercialProductResourceDto findByCode(String code);
}
