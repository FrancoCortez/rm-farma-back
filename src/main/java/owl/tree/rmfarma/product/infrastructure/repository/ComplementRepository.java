package owl.tree.rmfarma.product.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.product.infrastructure.entities.Complement;

import java.util.Optional;

@Repository
public interface ComplementRepository extends JpaRepository<Complement, String> {
    Optional<Complement> findByCode(String code);
}
