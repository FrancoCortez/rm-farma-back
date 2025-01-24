package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;

import java.util.Optional;

@Repository
public interface ViaRepository extends JpaRepository<Via, String> {

    Optional<Via> findByCode(String code);
}
