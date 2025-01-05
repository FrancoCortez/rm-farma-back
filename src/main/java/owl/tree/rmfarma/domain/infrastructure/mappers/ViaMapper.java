package owl.tree.rmfarma.domain.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;

@Mapper(componentModel = "spring")
public interface ViaMapper {

    ViaResourceDto toViaResourceDto(Via via);
}
