package owl.tree.rmfarma.domain.domain.ports.spi;

import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;

import java.util.List;

public interface ViaPersistencePort {
    List<ViaResourceDto> findAll();

    ViaResourceDto findByCode(String code);
}
