package owl.tree.rmfarma.doctor.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorUpdateResourceDto;
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

    default Doctor toEntityFromResource(DoctorResourceDto doctor) {
        if (doctor == null) return null;
        return Doctor.builder()
                .id(doctor.getId())
                .rut(doctor.getRut())
                .code(doctor.getCode())
                .name(doctor.getName())
                .enabled(doctor.getEnabled())
                .build();
    }

    default Doctor toUpdateEntity(Doctor current, DoctorUpdateResourceDto update) {
        if (current == null) return null;
        if (update == null) return current;
        if (update.getRut() != null) current.setRut(update.getRut());
        if (update.getName() != null) current.setName(update.getName().toUpperCase());
        return current;
    }
}
