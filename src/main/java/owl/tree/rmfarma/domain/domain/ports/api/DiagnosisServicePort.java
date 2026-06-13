package owl.tree.rmfarma.domain.domain.ports.api;

import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisCreateDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisUpdateResourceDto;

import java.util.List;

public interface DiagnosisServicePort {
    List<DiagnosisResourceDto> findAll();
    DiagnosisResourceDto findById(String id);
    DiagnosisResourceDto createDiagnosis(DiagnosisCreateDto dto);
    DiagnosisResourceDto updateDiagnosis(String id, DiagnosisUpdateResourceDto dto);
    void softDelete(String id);
}
