package owl.tree.rmfarma.service.domain.ports.spi;

import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;

import java.util.List;

public interface ServicesPersistencePort {
    ServiceResourceDto findByCode(String code);

    List<ServiceResourceDto> findAll();
}
