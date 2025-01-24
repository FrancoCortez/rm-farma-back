package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.manufacture.infrastructure.entities.GeneratorId;

import java.util.Optional;

@Repository
public interface GeneratorIdRepository extends JpaRepository<GeneratorId, String> {
    Optional<GeneratorId> findTopByYearOrderByCorrelativeDesc(Integer year);
}
