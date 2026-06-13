package owl.tree.rmfarma.doctor.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.spi.DoctorPersistencePort;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;
import owl.tree.rmfarma.doctor.infrastructure.mappers.DoctorMapper;
import owl.tree.rmfarma.doctor.infrastructure.repository.DoctorRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorPersistencePortAdapter implements DoctorPersistencePort {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public List<DoctorResourceDto> findAll() {
        return this.doctorRepository.findAllByEnabledTrue()
                .stream()
                .map(doctorMapper::toDoctorResourceDto)
                .toList();
    }

    public DoctorResourceDto findByRut(String rut) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.findByRutAndEnabledTrue(rut).orElse(null));
    }

    @Override
    public DoctorResourceDto findByRutIncludingDisabled(String rut) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.findByRut(rut).orElse(null));
    }

    @Override
    public DoctorResourceDto findByIdIncludingDisabled(String id) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.findById(id).orElse(null));
    }

    public DoctorResourceDto findById(String id) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.findByIdAndEnabledTrue(id).orElse(null));
    }

    public DoctorResourceDto createDoctor(DoctorCreateResourceDto create) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.save(doctorMapper.toEntity(create)));
    }

    @Override
    public DoctorResourceDto updateDoctor(Doctor doctor) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.save(doctor));
    }

    public DoctorResourceDto findTopByOrderByCodeDesc() {
        return doctorMapper.toDoctorResourceDto(doctorRepository.findTopByOrderByCodeDesc().orElse(null));
    }
}
