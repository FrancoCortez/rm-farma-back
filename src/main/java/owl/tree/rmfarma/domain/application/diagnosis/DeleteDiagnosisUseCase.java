package owl.tree.rmfarma.domain.application.diagnosis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.ports.api.DiagnosisServicePort;

@Component
@RequiredArgsConstructor
public class DeleteDiagnosisUseCase {
    private final DiagnosisServicePort diagnosisServicePort;

    public void softDelete(String id) {
        this.diagnosisServicePort.softDelete(id);
    }
}
