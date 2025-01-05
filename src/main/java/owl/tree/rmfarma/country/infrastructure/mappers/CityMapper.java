package owl.tree.rmfarma.country.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.country.domain.data.city.CityResourceDto;
import owl.tree.rmfarma.country.infrastructure.entities.City;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityResourceDto toCityResourceDto(City city);
}
