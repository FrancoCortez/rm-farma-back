package owl.tree.rmfarma.doctor.domain.ports.spi;

import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;

import java.util.List;

public interface DoctorPersistencePort {
    DoctorResourceDto findByRut(String rut);

    DoctorResourceDto findByRutIncludingDisabled(String rut);

    DoctorResourceDto findByIdIncludingDisabled(String id);

    List<DoctorResourceDto> findAll();

    DoctorResourceDto findById(String id);

    DoctorResourceDto findTopByOrderByCodeDesc();

    DoctorResourceDto createDoctor(DoctorCreateResourceDto create);

    DoctorResourceDto updateDoctor(Doctor doctor);
}
