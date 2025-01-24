package owl.tree.rmfarma.domain.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.IsaprePersistencePort;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;
import owl.tree.rmfarma.domain.infrastructure.mappers.IsapreMapper;
import owl.tree.rmfarma.domain.infrastructure.repository.IsapreRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IsaprePersistencePortAdapter implements IsaprePersistencePort {

    private final IsapreRepository isapreRepository;
    private final IsapreMapper isapreMapper;

    public List<IsapreResourceDto> findAll() {
        return this.isapreRepository.findAll()
                .stream()
                .map(this.isapreMapper::toIsapreResourceDto)
                .toList();
    }

    @Override
    public IsapreResourceDto findByCode(Integer code) {
        if (code == null) return null;
        Isapre isapre = this.isapreRepository.findByCode(code).orElse(null);
        if (isapre == null) return null;
        return this.isapreMapper.toIsapreResourceDto(isapre);
    }
}
