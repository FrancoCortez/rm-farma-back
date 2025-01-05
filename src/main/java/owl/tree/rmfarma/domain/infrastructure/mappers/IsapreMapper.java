package owl.tree.rmfarma.domain.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;

@Mapper(componentModel = "spring")
public interface IsapreMapper {

    IsapreResourceDto toIsapreResourceDto(Isapre isapre);
}
