package owl.tree.rmfarma.manufacture.domain.ports.spi;

import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface MasterOrderPersistencePort {
    List<MasterOrderResourceDto> findAll(OffsetDateTime searchDay, String searchIdentification);

    MasterOrderResourceDto create(MasterOrderCreateResourceDto masterOrderCreateResourceDto);

    MasterOrderResourceDto findById(String id);
}
