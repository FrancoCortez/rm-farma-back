package owl.tree.rmfarma.domain.domain.ports.spi;

import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;

import java.util.List;

public interface IsaprePersistencePort {
    List<IsapreResourceDto> findAll();

    IsapreResourceDto findByCode(String code);
}
