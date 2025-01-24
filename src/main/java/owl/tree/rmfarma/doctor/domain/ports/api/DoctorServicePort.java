package owl.tree.rmfarma.doctor.domain.ports.api;

import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;

import java.util.List;

public interface DoctorServicePort {

    //    DoctorResourceDto findByRut(String rut);
    List<DoctorResourceDto> findAll();

    DoctorResourceDto findById(String id);

    DoctorResourceDto createDoctor(DoctorCreateResourceDto create);
}
