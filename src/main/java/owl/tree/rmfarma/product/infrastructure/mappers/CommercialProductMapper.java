package owl.tree.rmfarma.product.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.infrastructure.entities.CommercialProduct;

@Mapper(componentModel = "spring")
public interface CommercialProductMapper {

    CommercialProductResourceDto toCommercialProductResourceDto(CommercialProduct entity);
}
