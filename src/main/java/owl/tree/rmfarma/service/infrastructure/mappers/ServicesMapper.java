package owl.tree.rmfarma.service.infrastructure.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

@Mapper(componentModel = "spring")
public interface ServicesMapper {

    ServiceResourceDto toServiceResourceDto(Services services);

    Services toServices(CreateServiceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateServiceRequest request, @MappingTarget Services services);
}
