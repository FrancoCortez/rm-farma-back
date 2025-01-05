package owl.tree.rmfarma.domain.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.Schema;

@Mapper(componentModel = "spring")
public interface SchemaMapper {

    SchemaResourceDto toSchemaResourceDto(Schema schema);
}
