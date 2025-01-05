package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.ClinicServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.ClinicPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicServicePort {

    private final ClinicPersistencePort clinicPersistencePort;

    public List<ClinicResourceDto> findAll() {
        return clinicPersistencePort.findAll();
    }

}
