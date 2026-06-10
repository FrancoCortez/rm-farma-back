package owl.tree.rmfarma.service.domain.ports.spi;

import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

import java.util.List;
import java.util.Optional;

public interface ServicesPersistencePort {
    Optional<Services> findById(String id);

    Optional<ServiceResourceDto> findResourceById(String id);

    List<ServiceResourceDto> findAll();

    Services save(Services entity);

    Optional<Services> findEnabledById(String id);

    void disableById(String id);

    boolean existsByCode(String code);

    boolean existsByDescription(String description);

    boolean existsByCodeAndIdNot(String code, String id);

    boolean existsByDescriptionAndIdNot(String description, String id);
}
