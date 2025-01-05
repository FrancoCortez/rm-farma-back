package owl.tree.rmfarma.domain.application.schema;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.SchemaServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindSchemaUseCase {
    private final SchemaServicePort schemaServicePort;

    public List<SchemaResourceDto> findAll() {
        return this.schemaServicePort.findAll();
    }
}
