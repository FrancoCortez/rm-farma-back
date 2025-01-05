package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.IsapreServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.IsaprePersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IsapreServiceImpl implements IsapreServicePort {
    private final IsaprePersistencePort isaprePersistencePort;

    public List<IsapreResourceDto> findAll() {
        return isaprePersistencePort.findAll();
    }
}
