package owl.tree.rmfarma.product.application.commercialproduct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.CommercialProductServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindCommercialProductUseCase {
    private final CommercialProductServicePort commercialProductServicePort;

    public List<CommercialProductResourceDto> findAll() {

        return this.commercialProductServicePort.findAll();
    }
}
