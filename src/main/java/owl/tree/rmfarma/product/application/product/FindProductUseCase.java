package owl.tree.rmfarma.product.application.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.ProductServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindProductUseCase {
    private final ProductServicePort productServicePort;

    public List<ProductResourceDto> findAll() {
        return productServicePort.findAll();
    }
}
