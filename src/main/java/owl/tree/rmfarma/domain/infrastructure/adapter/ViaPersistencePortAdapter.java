package owl.tree.rmfarma.domain.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.ViaPersistencePort;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;
import owl.tree.rmfarma.domain.infrastructure.mappers.ViaMapper;
import owl.tree.rmfarma.domain.infrastructure.repository.ViaRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ViaPersistencePortAdapter implements ViaPersistencePort {
    private final ViaRepository viaRepository;
    private final ViaMapper viaMapper;

    public List<ViaResourceDto> findAll() {
        return this.viaRepository.findAll()
                .stream()
                .map(this.viaMapper::toViaResourceDto)
                .toList();
    }

    @Override
    public ViaResourceDto findByCode(String code) {
        Via via = this.viaRepository.findByCode(code).orElse(null);
        if (via == null) return null;
        return this.viaMapper.toViaResourceDto(via);
    }
}
