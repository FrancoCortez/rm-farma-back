package owl.tree.rmfarma.service.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

@Mapper(componentModel = "spring")
public interface ServicesMapper {

    ServiceResourceDto toServiceResourceDto(Services services);
}
