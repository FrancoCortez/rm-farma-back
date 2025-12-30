package owl.tree.rmfarma.product.domain.ports.api;

import owl.tree.rmfarma.product.domain.data.product.ProductCreateDto;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;

import java.util.List;

public interface ProductServicePort {
    List<ProductResourceDto> findAll();
    ProductResourceDto createProduct (ProductCreateDto dto);
}
