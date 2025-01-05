package owl.tree.rmfarma.domain.application.documenttype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.documentype.DocumentTypeResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.DocumentTypeServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindDocumentTypeUseCase {
    private final DocumentTypeServicePort documentTypeServicePort;

    public List<DocumentTypeResourceDto> findAll() {
        return this.documentTypeServicePort.findAll();
    }
}
