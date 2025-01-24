package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;

import java.util.Optional;

@Repository
public interface IsapreRepository extends JpaRepository<Isapre, String> {
    Optional<Isapre> findByCode(Integer code);
}
