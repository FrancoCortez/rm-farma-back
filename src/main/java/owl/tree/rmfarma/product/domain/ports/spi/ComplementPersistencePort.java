package owl.tree.rmfarma.product.domain.ports.spi;

import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;

import java.util.List;

public interface ComplementPersistencePort {
    List<ComplementResourceDto> findAll();

    ComplementResourceDto findByCode(String complementCode);
}
