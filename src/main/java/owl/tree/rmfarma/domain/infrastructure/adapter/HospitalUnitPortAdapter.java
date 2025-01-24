package owl.tree.rmfarma.domain.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.HospitalUnitPersistencePort;
import owl.tree.rmfarma.domain.infrastructure.entities.HospitalUnit;
import owl.tree.rmfarma.domain.infrastructure.mappers.HospitalUnitMapper;
import owl.tree.rmfarma.domain.infrastructure.repository.HospitalUnitRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HospitalUnitPortAdapter implements HospitalUnitPersistencePort {
    private final HospitalUnitRepository hospitalUnitRepository;
    private final HospitalUnitMapper hospitalUnitMapper;

    @Override
    public List<HospitalUnitResourceDto> findAll() {
        return this.hospitalUnitRepository.findAll()
                .stream()
                .map(this.hospitalUnitMapper::toHospitalUnitResourceDto)
                .toList();
    }

    @Override
    public HospitalUnitResourceDto findByCode(String code) {
        if (code == null || code.isEmpty()) return null;
        HospitalUnit hospitalUnit = this.hospitalUnitRepository.findByCode(code).orElse(null);
        if (hospitalUnit == null) return null;
        return this.hospitalUnitMapper.toHospitalUnitResourceDto(hospitalUnit);
    }
}
