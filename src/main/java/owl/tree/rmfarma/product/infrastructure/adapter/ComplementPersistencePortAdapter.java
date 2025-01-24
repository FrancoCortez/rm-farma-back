package owl.tree.rmfarma.product.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;
import owl.tree.rmfarma.product.domain.ports.spi.ComplementPersistencePort;
import owl.tree.rmfarma.product.infrastructure.entities.Complement;
import owl.tree.rmfarma.product.infrastructure.mappers.ComplementMapper;
import owl.tree.rmfarma.product.infrastructure.repository.ComplementRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ComplementPersistencePortAdapter implements ComplementPersistencePort {

    private final ComplementRepository complementRepository;
    private final ComplementMapper complementMapper;

    @Override
    public List<ComplementResourceDto> findAll() {
        return this.complementRepository.findAll()
                .stream()
                .map(this.complementMapper::toComplementResourceDto)
                .toList();
    }

    @Override
    public ComplementResourceDto findByCode(String complementCode) {
        Complement complement = this.complementRepository.findByCode(complementCode).orElse(null);
        if (complement == null) return null;
        return this.complementMapper.toComplementResourceDto(complement);
    }
}
