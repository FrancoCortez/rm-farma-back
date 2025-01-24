package owl.tree.rmfarma.product.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.spi.CommercialProductPersistencePort;
import owl.tree.rmfarma.product.infrastructure.entities.CommercialProduct;
import owl.tree.rmfarma.product.infrastructure.mappers.CommercialProductMapper;
import owl.tree.rmfarma.product.infrastructure.repository.CommercialProductRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommercialProductPersistencePortAdapter implements CommercialProductPersistencePort {
    private final CommercialProductRepository commercialProductRepository;
    private final CommercialProductMapper commercialProductMapper;

    @Override
    public List<CommercialProductResourceDto> findAll() {
        return this.commercialProductRepository.findAll()
                .stream()
                .map(this.commercialProductMapper::toCommercialProductResourceDto)
                .toList();
    }

    @Override
    public CommercialProductResourceDto findByCode(String code) {
        CommercialProduct entity = this.commercialProductRepository.findByCode(code).orElse(null);
        if(entity == null) return null;
        return this.commercialProductMapper.toCommercialProductResourceDto(entity);
    }
}
