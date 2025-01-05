package owl.tree.rmfarma.domain.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.documentype.DocumentTypeResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.DocumentTypePersistencePort;
import owl.tree.rmfarma.domain.infrastructure.mappers.DocumentTypeMapper;
import owl.tree.rmfarma.domain.infrastructure.repository.DocumentTypeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentTypePersistencePortAdapter implements DocumentTypePersistencePort {

    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentTypeMapper documentTypeMapper;

    public List<DocumentTypeResourceDto> findAll() {
        return this.documentTypeRepository.findAll()
                .stream()
                .map(this.documentTypeMapper::toDocumentTypeResourceDto)
                .toList();
    }
}
