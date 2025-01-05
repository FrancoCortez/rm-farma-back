package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.ViaServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.ViaPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViaServiceImpl implements ViaServicePort {
    private final ViaPersistencePort viaPersistencePort;

    public List<ViaResourceDto> findAll() {
        return viaPersistencePort.findAll();
    }
}
