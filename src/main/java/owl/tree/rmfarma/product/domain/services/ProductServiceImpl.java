package owl.tree.rmfarma.product.domain.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaCreateDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.product.domain.data.product.ProductCreateDto;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.ProductServicePort;
import owl.tree.rmfarma.product.domain.ports.spi.ProductPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductServicePort {
    private final ProductPersistencePort productPersistencePort;

    @Transactional
    public List<ProductResourceDto> findAll() {
        return this.productPersistencePort.findAll();
    }


    public ProductResourceDto createProduct (ProductCreateDto dto) {
        ProductResourceDto getByCode = this.productPersistencePort.findByCode(dto.getCode());
        if(getByCode != null) {
            throw new ExistsException(getByCode.getDescription(), "PA", getByCode.getCode());
        }
        return this.productPersistencePort.createProduct(dto);
    }
}
