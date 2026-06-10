package owl.tree.rmfarma.service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, String> {

    Optional<Services> findByCode(String code);

    List<Services> findAllByEnabledTrue();

    Optional<Services> findByCodeAndEnabledTrue(String code);

    boolean existsByCode(String code);

    boolean existsByDescription(String description);

    Optional<Services> findByDescriptionAndEnabledTrue(String description);

    boolean existsByCodeAndIdNot(String code, String id);

    boolean existsByDescriptionAndIdNot(String description, String id);
}
