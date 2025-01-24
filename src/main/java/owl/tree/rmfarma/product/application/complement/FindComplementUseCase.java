package owl.tree.rmfarma.product.application.complement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.ComplementServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindComplementUseCase {

    private final ComplementServicePort complementServicePort;

    public List<ComplementResourceDto> findAll() {
        return this.complementServicePort.findAll();
    }
}
