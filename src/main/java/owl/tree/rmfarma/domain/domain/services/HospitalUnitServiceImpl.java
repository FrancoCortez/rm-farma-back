package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.HospitalUnitServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.HospitalUnitPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalUnitServiceImpl implements HospitalUnitServicePort {

    private final HospitalUnitPersistencePort hospitalUnitPersistencePort;

    @Override
    public List<HospitalUnitResourceDto> findAll() {
        return this.hospitalUnitPersistencePort.findAll();
    }
}
