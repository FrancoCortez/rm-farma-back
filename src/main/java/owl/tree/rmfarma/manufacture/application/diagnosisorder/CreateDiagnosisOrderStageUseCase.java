package owl.tree.rmfarma.manufacture.application.diagnosisorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.application.diagnosisorder.data.DiagnosisOrderStageCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.api.DiagnosisOrderStageServicePort;

@Component
@RequiredArgsConstructor
public class CreateDiagnosisOrderStageUseCase {
    private final DiagnosisOrderStageServicePort diagnosisOrderStageServicePort;

    public DiagnosisOrderStageResourceDto createDiagnosis(DiagnosisOrderStageCreateResourceUseCaseDto body) {
        return this.diagnosisOrderStageServicePort.createDiagnosisOrder(body);
    }
}
