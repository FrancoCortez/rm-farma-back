package owl.tree.rmfarma.product.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductCreateDto;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.CommercialProductServicePort;
import owl.tree.rmfarma.product.domain.ports.spi.CommercialProductPersistencePort;
import owl.tree.rmfarma.product.domain.ports.spi.ProductPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommercialProductServiceImpl implements CommercialProductServicePort {
    private final CommercialProductPersistencePort commercialProductPersistencePort;
    private final ProductPersistencePort productPersistencePort;

    @Override
    public List<CommercialProductResourceDto> findAll() {
        return this.commercialProductPersistencePort.findAll();
    }

    @Override
    public CommercialProductResourceDto createCommercialProduct(CommercialProductCreateDto dto) {
        CommercialProductResourceDto commercialProductResourceDto = this.commercialProductPersistencePort.findByCode(dto.getCode());
        if(commercialProductResourceDto != null) {
            throw new ExistsException(commercialProductResourceDto.getDescription(), "Forma Comercial", commercialProductResourceDto.getCode());
        }
        ProductResourceDto productResourceDto = this.productPersistencePort.findByCode(dto.getActiveIngredientCode());
        if(productResourceDto == null) {
            throw new NotFoundException("PA", dto.getActiveIngredientCode());
        }
        dto.setActiveIngredientCode(productResourceDto.getId());
        return this.commercialProductPersistencePort.createCommercialProduct(dto);
    }
}
