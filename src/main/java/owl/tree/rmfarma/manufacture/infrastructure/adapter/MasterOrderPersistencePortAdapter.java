package owl.tree.rmfarma.manufacture.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.spi.MasterOrderPersistencePort;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;
import owl.tree.rmfarma.manufacture.infrastructure.mappers.MasterOrderMapper;
import owl.tree.rmfarma.manufacture.infrastructure.repository.MasterOrderRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MasterOrderPersistencePortAdapter implements MasterOrderPersistencePort {
    private final MasterOrderRepository masterOrderRepository;
    private final MasterOrderMapper masterOrderMapper;

    public List<MasterOrderResourceDto> findAll(OffsetDateTime searchDay, String searchIdentification) {
        OffsetDateTime startOfDay = searchDay.toLocalDate().atStartOfDay().atOffset(searchDay.getOffset());
        OffsetDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        List<MasterOrder> masterOrders;
        if (searchIdentification != null && !searchIdentification.isEmpty()) {
            masterOrders = this.masterOrderRepository.findAllByPatientIdentificationAndProductionDateBetween(
                    searchIdentification, startOfDay, endOfDay);
        } else {
            masterOrders = this.masterOrderRepository.findAllByProductionDateBetween(startOfDay, endOfDay);
        }
        return masterOrders.stream()
                .map(this.masterOrderMapper::toMasterResourceDto)
                .toList();
    }

    public MasterOrderResourceDto create(MasterOrderCreateResourceDto masterOrderCreateResourceDto) {
        return this.masterOrderMapper.toMasterResourceDto(this.masterOrderRepository.save(this.masterOrderMapper.toMasterOrderEntity(masterOrderCreateResourceDto)));
    }

    public MasterOrderResourceDto findById(String id) {
        MasterOrder masterOrder = this.masterOrderRepository.findById(id).orElse(null);
        if (masterOrder == null) return null;
        return this.masterOrderMapper.toMasterResourceDto(masterOrder);
    }
}
