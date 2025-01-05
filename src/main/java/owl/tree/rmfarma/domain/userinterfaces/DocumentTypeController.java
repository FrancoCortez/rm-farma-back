package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.domain.application.documenttype.FindDocumentTypeUseCase;
import owl.tree.rmfarma.domain.domain.data.documentype.DocumentTypeResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/document-type")
@RequiredArgsConstructor
public class DocumentTypeController {
    private final FindDocumentTypeUseCase findDocumentTypeUseCase;

    @GetMapping
    public ResponseEntity<List<DocumentTypeResourceDto>> findAll() {
        return ResponseEntity.ok(this.findDocumentTypeUseCase.findAll());
    }
}
