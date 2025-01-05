package owl.tree.rmfarma.domain.domain.ports.spi;

import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;

import java.util.List;

public interface ClinicPersistencePort {
    List<ClinicResourceDto> findAll();

    ClinicResourceDto findByCode(String code);
}
