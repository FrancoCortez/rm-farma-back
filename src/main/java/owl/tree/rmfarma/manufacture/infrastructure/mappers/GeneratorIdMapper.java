package owl.tree.rmfarma.manufacture.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.manufacture.domain.data.generateid.GeneratorIdCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.generateid.GeneratorIdResourceDto;
import owl.tree.rmfarma.manufacture.infrastructure.entities.GeneratorId;

@Mapper(componentModel = "spring")
public interface GeneratorIdMapper {

    GeneratorIdResourceDto toGenerateIdResourceDto(GeneratorId entity);

    GeneratorId toGeneratorId(GeneratorIdCreateResourceDto dto);
}
