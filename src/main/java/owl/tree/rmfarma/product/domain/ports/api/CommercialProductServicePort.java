package owl.tree.rmfarma.product.domain.ports.api;

import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductCreateDto;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;

import java.util.List;

public interface CommercialProductServicePort {
    List<CommercialProductResourceDto> findAll();

    CommercialProductResourceDto createCommercialProduct(CommercialProductCreateDto dto);
}
