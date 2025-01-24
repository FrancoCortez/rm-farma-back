package owl.tree.rmfarma.product.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.ProductServicePort;
import owl.tree.rmfarma.product.domain.ports.spi.ProductPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductServicePort {
    private final ProductPersistencePort productPersistencePort;

    public List<ProductResourceDto> findAll() {
        return this.productPersistencePort.findAll();
    }
}
