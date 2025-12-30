package owl.tree.rmfarma.product.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductCreateDto;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.infrastructure.entities.CommercialProduct;
import owl.tree.rmfarma.product.infrastructure.entities.Product;

@Mapper(componentModel = "spring")
public interface CommercialProductMapper {

    CommercialProductResourceDto toCommercialProductResourceDto(CommercialProduct entity);

    default CommercialProduct toCommercialProduct(CommercialProductCreateDto dto) {
        if ( dto == null ) {
            return null;
        }
        CommercialProduct commercialProduct = new CommercialProduct();
        commercialProduct.setCode( dto.getCode() );
        commercialProduct.setDescription( dto.getDescription() );
        commercialProduct.setConcentration( dto.getConcentration() );
        commercialProduct.setConcentrationUnit( dto.getConcentrationUnit() );
        commercialProduct.setLaboratory( dto.getLaboratory() );
        Product product = new Product();
        product.setId(dto.getActiveIngredientCode());
        commercialProduct.setProduct(product);
        return commercialProduct;
    }
}
