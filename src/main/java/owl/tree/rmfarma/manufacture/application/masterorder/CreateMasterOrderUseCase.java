package owl.tree.rmfarma.manufacture.application.masterorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.application.masterorder.data.MasterOrderCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.api.MasterOrderServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateMasterOrderUseCase {

    private final MasterOrderServicePort masterOrderServicePort;


    public MasterOrderResourceDto create(List<MasterOrderCreateResourceUseCaseDto> masterOrderCreateResourceUseCaseDto) {
        MasterOrderResourceDto masterOrderResourceDto = new MasterOrderResourceDto();
        for (MasterOrderCreateResourceUseCaseDto dto : masterOrderCreateResourceUseCaseDto) {
            masterOrderResourceDto = this.masterOrderServicePort.create(dto);
        }
        return masterOrderResourceDto;
    }
}
