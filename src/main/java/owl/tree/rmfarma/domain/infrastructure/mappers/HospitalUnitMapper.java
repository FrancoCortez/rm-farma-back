package owl.tree.rmfarma.domain.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.HospitalUnit;

@Mapper(componentModel = "spring")
public interface HospitalUnitMapper {
    HospitalUnitResourceDto toHospitalUnitResourceDto(HospitalUnit clinic);
}
