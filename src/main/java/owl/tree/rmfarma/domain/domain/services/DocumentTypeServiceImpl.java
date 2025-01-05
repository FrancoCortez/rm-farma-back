package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.documentype.DocumentTypeResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.DocumentTypeServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.DocumentTypePersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentTypeServiceImpl implements DocumentTypeServicePort {
    private final DocumentTypePersistencePort documentTypePersistencePort;

    public List<DocumentTypeResourceDto> findAll() {
        return documentTypePersistencePort.findAll();
    }
}
