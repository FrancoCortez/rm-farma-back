package owl.tree.rmfarma.domain.domain.ports.api;

import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;

import java.util.List;

public interface HospitalUnitServicePort {
    List<HospitalUnitResourceDto> findAll();
}
