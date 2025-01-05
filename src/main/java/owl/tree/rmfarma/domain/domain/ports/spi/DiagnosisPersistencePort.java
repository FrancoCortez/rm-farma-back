package owl.tree.rmfarma.domain.domain.ports.spi;

import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;

import java.util.List;

public interface DiagnosisPersistencePort {

    List<DiagnosisResourceDto> findAll();

    DiagnosisResourceDto findByCode(String code);
}
