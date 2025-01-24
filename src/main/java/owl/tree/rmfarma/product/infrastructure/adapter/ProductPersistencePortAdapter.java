package owl.tree.rmfarma.product.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.spi.ProductPersistencePort;
import owl.tree.rmfarma.product.infrastructure.entities.Product;
import owl.tree.rmfarma.product.infrastructure.mappers.ProductMapper;
import owl.tree.rmfarma.product.infrastructure.repository.ProductRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductPersistencePortAdapter implements ProductPersistencePort {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResourceDto> findAll() {
        return this.productRepository.findAll()
                .stream()
                .map(productMapper::toProductResourceDto)
                .toList();
    }

    @Override
    public ProductResourceDto findByCode(String code) {
        Product product = this.productRepository.findByCode(code).orElse(null);
        if (product == null) return null;
        return this.productMapper.toProductResourceDto(product);
    }
}
