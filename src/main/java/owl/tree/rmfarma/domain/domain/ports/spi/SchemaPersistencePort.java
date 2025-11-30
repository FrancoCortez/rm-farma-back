package owl.tree.rmfarma.domain.domain.ports.spi;

import owl.tree.rmfarma.domain.domain.data.schema.SchemaCreateDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;

import java.util.List;

public interface SchemaPersistencePort {
    List<SchemaResourceDto> findAll();
    SchemaResourceDto findByCode(String schema);
    SchemaResourceDto createSchema(SchemaCreateDto dto);
}
