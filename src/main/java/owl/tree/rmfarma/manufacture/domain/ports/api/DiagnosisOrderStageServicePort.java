package owl.tree.rmfarma.manufacture.domain.ports.api;

import owl.tree.rmfarma.manufacture.application.diagnosisorder.data.DiagnosisOrderStageCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;

public interface DiagnosisOrderStageServicePort {
    DiagnosisOrderStageResourceDto createDiagnosisOrder(DiagnosisOrderStageCreateResourceUseCaseDto body);
}
