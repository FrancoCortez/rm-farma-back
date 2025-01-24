package owl.tree.rmfarma.doctor.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorResourceDto toDoctorResourceDto(Doctor doctor);

    default Doctor toEntity(DoctorCreateResourceDto doctor) {
        if (doctor == null) return null;
        return Doctor.builder()
                .rut(doctor.getRut())
                .code(doctor.getCode())
                .name(doctor.getName().toUpperCase())
                .build();
    }
}
