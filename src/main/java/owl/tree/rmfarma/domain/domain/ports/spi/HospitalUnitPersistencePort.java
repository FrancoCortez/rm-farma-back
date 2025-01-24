package owl.tree.rmfarma.domain.domain.ports.spi;

import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;

import java.util.List;

public interface HospitalUnitPersistencePort {
    List<HospitalUnitResourceDto> findAll();

    HospitalUnitResourceDto findByCode(String code);
}
