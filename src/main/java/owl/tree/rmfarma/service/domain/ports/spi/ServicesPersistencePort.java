package owl.tree.rmfarma.service.domain.ports.spi;

import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

import java.util.List;
import java.util.Optional;

public interface ServicesPersistencePort {
    Optional<Services> findByCode(String code);

    Optional<ServiceResourceDto> findResourceByCode(String code);

    List<ServiceResourceDto> findAll();

    Services save(Services entity);

    Optional<Services> findEnabledByCode(String code);

    void disableByCode(String code);

    boolean existsByCode(String code);

    boolean existsByDescription(String description);

    boolean existsByCodeAndIdNot(String code, String id);

    boolean existsByDescriptionAndIdNot(String description, String id);
}
