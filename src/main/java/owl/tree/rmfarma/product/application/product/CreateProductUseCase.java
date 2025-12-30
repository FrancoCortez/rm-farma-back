package owl.tree.rmfarma.product.application.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.product.domain.data.product.ProductCreateDto;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.ProductServicePort;

@Component
@RequiredArgsConstructor
public class CreateProductUseCase {
    private final ProductServicePort productServicePort;


    public ProductResourceDto createProduct(ProductCreateDto dto) {
        return this.productServicePort.createProduct(dto);
    }
}
