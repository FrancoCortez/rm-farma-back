package owl.tree.rmfarma.manufacture.domain.ports.spi;

import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;

public interface DiagnosisOrderStagePersistencePort {
    DiagnosisOrderStageResourceDto createDiagnosisOrder(DiagnosisOrderStageCreateResourceDto body);

    DiagnosisOrderStageResourceDto findById(String id);

}
