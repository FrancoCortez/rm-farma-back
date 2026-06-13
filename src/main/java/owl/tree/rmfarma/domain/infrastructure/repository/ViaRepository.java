package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViaRepository extends JpaRepository<Via, String> {

    Optional<Via> findByCode(String code);

    List<Via> findAllByEnabledTrue();

    Optional<Via> findByCodeAndEnabledTrue(String code);
}
