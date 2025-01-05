package owl.tree.rmfarma.domain.domain.ports.spi;

import owl.tree.rmfarma.domain.domain.data.documentype.DocumentTypeResourceDto;

import java.util.List;

public interface DocumentTypePersistencePort {
    List<DocumentTypeResourceDto> findAll();
}
