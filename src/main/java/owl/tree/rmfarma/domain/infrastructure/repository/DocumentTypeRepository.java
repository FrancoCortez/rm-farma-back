package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;
import owl.tree.rmfarma.domain.infrastructure.entities.DocumentType;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {
}
