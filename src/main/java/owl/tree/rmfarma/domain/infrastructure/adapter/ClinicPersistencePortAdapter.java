package owl.tree.rmfarma.domain.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.ClinicPersistencePort;
import owl.tree.rmfarma.domain.infrastructure.entities.Clinic;
import owl.tree.rmfarma.domain.infrastructure.mappers.ClinicMapper;
import owl.tree.rmfarma.domain.infrastructure.repository.ClinicRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClinicPersistencePortAdapter implements ClinicPersistencePort {

    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    public List<ClinicResourceDto> findAll() {
        return this.clinicRepository.findAll()
                .stream()
                .map(this.clinicMapper::toClinicResourceDto)
                .toList();
    }

    public ClinicResourceDto findByCode(String code) {
        if(code == null || code.isEmpty()) return null;
        Clinic clinic = this.clinicRepository.findByCode(code).orElse(null);
        if(clinic == null) return null;
        return this.clinicMapper.toClinicResourceDto(clinic);
    }
}
