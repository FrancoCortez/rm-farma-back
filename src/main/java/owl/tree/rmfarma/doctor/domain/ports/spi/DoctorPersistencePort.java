package owl.tree.rmfarma.doctor.domain.ports.spi;

import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;

import java.util.List;

public interface DoctorPersistencePort {
    DoctorResourceDto findByRut(String rut);
    List<DoctorResourceDto> findAll();
    DoctorResourceDto findById(String id);
}
