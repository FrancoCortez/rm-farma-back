package owl.tree.rmfarma.domain.domain.ports.api;

import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;

import java.util.List;

public interface SchemaServicePort {
    List<SchemaResourceDto> findAll();
}
