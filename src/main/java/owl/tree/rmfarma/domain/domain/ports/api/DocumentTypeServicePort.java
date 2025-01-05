package owl.tree.rmfarma.domain.domain.ports.api;

import owl.tree.rmfarma.domain.domain.data.documentype.DocumentTypeResourceDto;

import java.util.List;

public interface DocumentTypeServicePort {
    List<DocumentTypeResourceDto> findAll();
}
