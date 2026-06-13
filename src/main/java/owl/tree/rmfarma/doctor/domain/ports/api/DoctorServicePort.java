package owl.tree.rmfarma.doctor.domain.ports.api;

import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorUpdateResourceDto;

import java.util.List;

public interface DoctorServicePort {

    List<DoctorResourceDto> findAll();

    DoctorResourceDto findById(String id);

    DoctorResourceDto createDoctor(DoctorCreateResourceDto create);

    DoctorResourceDto updateDoctor(String id, DoctorUpdateResourceDto update);

    DoctorResourceDto deleteDoctor(String id);
}
