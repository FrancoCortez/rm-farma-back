package owl.tree.rmfarma.domain.domain.ports.spi;

import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisCreateDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisUpdateResourceDto;

import java.util.List;

public interface DiagnosisPersistencePort {

    List<DiagnosisResourceDto> findAll();

    DiagnosisResourceDto findByCode(String code);

    DiagnosisResourceDto findByCodeIncludingDisabled(String code);

    DiagnosisResourceDto findById(String id);

    DiagnosisResourceDto createDiagnosis(DiagnosisCreateDto dto);

    DiagnosisResourceDto updateDiagnosis(String id, DiagnosisUpdateResourceDto dto);

    void softDelete(String id);
}
