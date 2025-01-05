package owl.tree.rmfarma.service.domain.ports.spi;

import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;

public interface ServicesPersistencePort {
    ServiceResourceDto findByCode(String code);
}
