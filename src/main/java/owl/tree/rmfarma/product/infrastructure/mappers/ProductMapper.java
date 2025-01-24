package owl.tree.rmfarma.product.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.infrastructure.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResourceDto toProductResourceDto(Product entity);
}
