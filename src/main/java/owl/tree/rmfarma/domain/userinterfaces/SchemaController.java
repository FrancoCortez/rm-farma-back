package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.domain.application.schema.FindSchemaUseCase;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/schemas")
@RequiredArgsConstructor
public class SchemaController {

    private final FindSchemaUseCase findSchemaUseCase;

    @GetMapping
    public ResponseEntity<List<SchemaResourceDto>> findAll() {
        return ResponseEntity.ok(this.findSchemaUseCase.findAll());
    }
}
