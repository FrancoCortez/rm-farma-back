package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.domain.application.schema.CreateSchemaUseCase;
import owl.tree.rmfarma.domain.application.schema.FindSchemaUseCase;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaCreateDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/schemas")
@RequiredArgsConstructor
public class SchemaController {

    private final FindSchemaUseCase findSchemaUseCase;
    private final CreateSchemaUseCase createSchemaUseCase;

    @GetMapping
    public ResponseEntity<List<SchemaResourceDto>> findAll() {
        return ResponseEntity.ok(this.findSchemaUseCase.findAll());
    }

    @PostMapping
    public ResponseEntity<SchemaResourceDto> createSchema (@RequestBody SchemaCreateDto dto) {
        return ResponseEntity.ok(this.createSchemaUseCase.createSchema(dto));
    }
}
