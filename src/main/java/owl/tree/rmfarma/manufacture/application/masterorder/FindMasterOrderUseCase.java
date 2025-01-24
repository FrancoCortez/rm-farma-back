package owl.tree.rmfarma.manufacture.application.masterorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.api.MasterOrderServicePort;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindMasterOrderUseCase {
    private final MasterOrderServicePort masterOrderServicePort;

    public List<MasterOrderResourceDto> findAll(OffsetDateTime searchDay, String searchIdentification) {
        return this.masterOrderServicePort.findAll(searchDay, searchIdentification);
    }
}
