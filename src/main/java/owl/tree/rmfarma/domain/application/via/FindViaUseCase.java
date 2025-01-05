package owl.tree.rmfarma.domain.application.via;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.ViaServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindViaUseCase {
    private final ViaServicePort viaServicePort;

    public List<ViaResourceDto> findAll() {
        return this.viaServicePort.findAll();
    }
}
