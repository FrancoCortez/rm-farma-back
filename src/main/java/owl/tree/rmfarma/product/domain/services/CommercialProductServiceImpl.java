package owl.tree.rmfarma.product.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.CommercialProductServicePort;
import owl.tree.rmfarma.product.domain.ports.spi.CommercialProductPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommercialProductServiceImpl implements CommercialProductServicePort {
    private final CommercialProductPersistencePort commercialProductPersistencePort;

    @Override
    public List<CommercialProductResourceDto> findAll() {
        return this.commercialProductPersistencePort.findAll();
    }
}
