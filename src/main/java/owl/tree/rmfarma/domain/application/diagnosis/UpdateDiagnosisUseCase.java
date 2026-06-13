package owl.tree.rmfarma.domain.application.diagnosis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisUpdateResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.DiagnosisServicePort;

@Component
@RequiredArgsConstructor
public class UpdateDiagnosisUseCase {
    private final DiagnosisServicePort diagnosisServicePort;

    public DiagnosisResourceDto update(String id, DiagnosisUpdateResourceDto dto) {
        return this.diagnosisServicePort.updateDiagnosis(id, dto);
    }
}
