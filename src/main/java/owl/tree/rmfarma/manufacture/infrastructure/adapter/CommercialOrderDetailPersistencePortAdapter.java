package owl.tree.rmfarma.manufacture.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.spi.CommercialOrderDetailPersistencePort;
import owl.tree.rmfarma.manufacture.infrastructure.mappers.CommercialOrderDetailMapper;
import owl.tree.rmfarma.manufacture.infrastructure.repository.CommercialOrderDetailRepository;

@Component
@RequiredArgsConstructor
public class CommercialOrderDetailPersistencePortAdapter implements CommercialOrderDetailPersistencePort {
    private final CommercialOrderDetailRepository commercialOrderDetailRepository;
    private final CommercialOrderDetailMapper commercialOrderDetailMapper;

    @Override
    public CommercialOrderDetailResourceDto save(CommercialOrderDetailCreateResourceDto commercialOrderDetail) {
        return this.commercialOrderDetailMapper.toResourceDto(this.commercialOrderDetailRepository.save(this.commercialOrderDetailMapper.toCommercialOrderDetailEntity(commercialOrderDetail)));
    }
}
