package owl.tree.rmfarma.domain.application.schema;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaCreateDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.SchemaServicePort;

@Component
@RequiredArgsConstructor
public class CreateSchemaUseCase {

    private final SchemaServicePort schemaServicePort;

    public SchemaResourceDto createSchema (SchemaCreateDto dto) {
        return schemaServicePort.createSchema(dto);
    }
}
