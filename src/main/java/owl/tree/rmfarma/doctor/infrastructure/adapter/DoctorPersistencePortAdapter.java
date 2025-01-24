package owl.tree.rmfarma.doctor.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.spi.DoctorPersistencePort;
import owl.tree.rmfarma.doctor.infrastructure.mappers.DoctorMapper;
import owl.tree.rmfarma.doctor.infrastructure.repository.DoctorRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorPersistencePortAdapter implements DoctorPersistencePort {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public List<DoctorResourceDto> findAll() {
        return this.doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toDoctorResourceDto)
                .toList();
    }

    public DoctorResourceDto findByRut(String rut) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.findByRut(rut).orElse(null));
    }

    public DoctorResourceDto findById(String id) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.findById(id).orElse(null));
    }

    public DoctorResourceDto createDoctor(DoctorCreateResourceDto create) {
        return doctorMapper.toDoctorResourceDto(doctorRepository.save(doctorMapper.toEntity(create)));
    }

    public DoctorResourceDto findTopByOrderByCodeDesc() {
        return doctorMapper.toDoctorResourceDto(doctorRepository.findTopByOrderByCodeDesc().orElse(null));
    }
}
