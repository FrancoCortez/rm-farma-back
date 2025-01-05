package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;
import owl.tree.rmfarma.domain.infrastructure.entities.Schema;

import java.util.Optional;

@Repository
public interface SchemaRepository extends JpaRepository<Schema, String> {

    Optional<Schema> findByCode(String code);
}
