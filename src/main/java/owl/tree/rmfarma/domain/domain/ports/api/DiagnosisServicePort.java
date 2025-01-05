package owl.tree.rmfarma.domain.domain.ports.api;

import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;

import java.util.List;

public interface DiagnosisServicePort {
    List<DiagnosisResourceDto> findAll();
}
