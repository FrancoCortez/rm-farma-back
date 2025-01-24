package owl.tree.rmfarma.product.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;
import owl.tree.rmfarma.product.domain.ports.api.ComplementServicePort;
import owl.tree.rmfarma.product.domain.ports.spi.ComplementPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplementServiceImpl implements ComplementServicePort {

    private final ComplementPersistencePort complementPersistencePort;

    @Override
    public List<ComplementResourceDto> findAll() {
        return this.complementPersistencePort.findAll();
    }
}
