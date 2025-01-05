package owl.tree.rmfarma.domain.domain.ports.api;

import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;

import java.util.List;

public interface ClinicServicePort {
    List<ClinicResourceDto> findAll();
}
