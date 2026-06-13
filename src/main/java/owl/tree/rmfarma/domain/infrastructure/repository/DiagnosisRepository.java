package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, String> {
    Optional<Diagnosis> findByCode(String code);

    List<Diagnosis> findAllByEnabledTrue();

    Optional<Diagnosis> findByCodeAndEnabledTrue(String code);

    Optional<Diagnosis> findByIdAndEnabledTrue(String id);

    boolean existsByCode(String code);

    boolean existsByDescription(String description);
}
