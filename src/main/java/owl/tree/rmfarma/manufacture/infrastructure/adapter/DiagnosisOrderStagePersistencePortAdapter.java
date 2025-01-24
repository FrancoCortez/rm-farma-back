package owl.tree.rmfarma.manufacture.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.spi.DiagnosisOrderStagePersistencePort;
import owl.tree.rmfarma.manufacture.infrastructure.entities.DiagnosisOrderStage;
import owl.tree.rmfarma.manufacture.infrastructure.mappers.DiagnosisOrderStageMapper;
import owl.tree.rmfarma.manufacture.infrastructure.repository.DiagnosisOrderStageRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DiagnosisOrderStagePersistencePortAdapter implements DiagnosisOrderStagePersistencePort {
    private final DiagnosisOrderStageRepository diagnosisOrderStageRepository;
    private final DiagnosisOrderStageMapper diagnosisOrderStageMapper;

    @Override
    public DiagnosisOrderStageResourceDto createDiagnosisOrder(DiagnosisOrderStageCreateResourceDto body) {
        return this.diagnosisOrderStageMapper.toDiagnosisOrderResourceDto(this.diagnosisOrderStageRepository.save(this.diagnosisOrderStageMapper.toDiagnosisOrderEntity(body)));
    }

    @Override
    public DiagnosisOrderStageResourceDto findById(String id) {
        DiagnosisOrderStage response = this.diagnosisOrderStageRepository.findByIdWithPatientAndDiagnosisPatient(id).orElse(null);
        if(response == null) return null;
        return this.diagnosisOrderStageMapper.toDiagnosisOrderResourceDto(response);

    }
}
