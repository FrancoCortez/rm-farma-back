package owl.tree.rmfarma.doctor.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorResourceDto toDoctorResourceDto(Doctor doctor);
}
