package owl.tree.rmfarma.domain.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.Clinic;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    ClinicResourceDto toClinicResourceDto(Clinic clinic);
}
