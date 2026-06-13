package owl.tree.rmfarma.domain.application.diagnosis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisCreateDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.DiagnosisServicePort;

@Component
@RequiredArgsConstructor
public class CreateDiagnosisUseCase {
    private final DiagnosisServicePort diagnosisServicePort;

    public DiagnosisResourceDto create(DiagnosisCreateDto dto) {
        return this.diagnosisServicePort.createDiagnosis(dto);
    }
}
