package owl.tree.rmfarma.product.domain.ports.spi;

import owl.tree.rmfarma.product.domain.data.product.ProductCreateDto;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;

import java.util.List;

public interface ProductPersistencePort {
    List<ProductResourceDto> findAll();
    ProductResourceDto findByCode(String code);
    ProductResourceDto createProduct (ProductCreateDto dto);
}
