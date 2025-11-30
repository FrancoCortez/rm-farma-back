package owl.tree.rmfarma.domain.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaCreateDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.SchemaPersistencePort;
import owl.tree.rmfarma.domain.infrastructure.entities.Schema;
import owl.tree.rmfarma.domain.infrastructure.mappers.SchemaMapper;
import owl.tree.rmfarma.domain.infrastructure.repository.SchemaRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchemaPersistencePortAdapter implements SchemaPersistencePort {
    private final SchemaRepository schemaRepository;
    private final SchemaMapper schemaMapper;

    public List<SchemaResourceDto> findAll() {
        return this.schemaRepository.findAll()
                .stream()
                .map(this.schemaMapper::toSchemaResourceDto)
                .toList();
    }

    public SchemaResourceDto findByCode(String code) {
        if (code == null || code.isEmpty()) return null;
        Schema schema = this.schemaRepository.findByCode(code).orElse(null);
        if (schema == null) return null;
        return this.schemaMapper.toSchemaResourceDto(schema);
    }

    public SchemaResourceDto createSchema(SchemaCreateDto dto) {
        return this.schemaMapper.toSchemaResourceDto(this.schemaRepository.save(this.schemaMapper.toSchemaEntity(dto)));
    }
}
