package owl.tree.rmfarma.domain.application.diagnosis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.DiagnosisServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindDiagnosisUseCase {
    private final DiagnosisServicePort diagnosisServicePort;

    public List<DiagnosisResourceDto> findAll() {
        return this.diagnosisServicePort.findAll();
    }

    public DiagnosisResourceDto findById(String id) {
        return this.diagnosisServicePort.findById(id);
    }
}
