package owl.tree.rmfarma.domain.application.isapre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.IsapreServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindIsapreUseCase {
    private final IsapreServicePort isapreServicePort;

    public List<IsapreResourceDto> findAll() {
        return this.isapreServicePort.findAll();
    }
}
