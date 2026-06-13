package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.DocumentType;

import java.util.List;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {

    List<DocumentType> findAllByEnabledTrue();
}
