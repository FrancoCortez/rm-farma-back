package owl.tree.rmfarma.product.application.commercialproduct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductCreateDto;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.CommercialProductServicePort;

@Component
@RequiredArgsConstructor
public class CreateCommercialProductUseCase {

    private final CommercialProductServicePort commercialProductServicePort;

    public CommercialProductResourceDto createCommercialProduct(CommercialProductCreateDto dto) {
        return this.commercialProductServicePort.createCommercialProduct(dto);
    }
}
