package owl.tree.rmfarma.manufacture.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import owl.tree.rmfarma.manufacture.application.diagnosisorder.data.DiagnosisOrderStageCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.InternalMasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.api.DiagnosisOrderStageServicePort;
import owl.tree.rmfarma.manufacture.domain.ports.api.MasterOrderServicePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.DiagnosisOrderStagePersistencePort;
import owl.tree.rmfarma.patient.domain.ports.spi.DiagnosisPatientPersistencePort;
import owl.tree.rmfarma.shared.enumes.StateMachineEnum;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class DiagnosisOrderStageServiceImpl implements DiagnosisOrderStageServicePort {
    private final DiagnosisOrderStagePersistencePort diagnosisOrderStagePersistencePort;
    private final MasterOrderServicePort masterOrderServicePort;
    private final DiagnosisPatientPersistencePort diagnosisPatientPersistencePort;

    @Override
    @Transactional
    public DiagnosisOrderStageResourceDto createDiagnosisOrder(DiagnosisOrderStageCreateResourceUseCaseDto body) {
        DiagnosisOrderStageCreateResourceDto resource = getDiagnosisOrderStageCreateResourceDto(body);
        DiagnosisOrderStageResourceDto response = this.diagnosisOrderStagePersistencePort.createDiagnosisOrder(resource);
        DiagnosisOrderStageResourceDto responseBd = this.diagnosisOrderStagePersistencePort.findById(response.getId());
        InternalMasterOrderCreateResourceDto internal = InternalMasterOrderCreateResourceDto.builder()
                .patientIdentification(body.getPatientIdentification())
                .status(StateMachineEnum.CHECK_PATIENT_OK)
                .productionDate(body.getProductionDate())
                .build();
        this.masterOrderServicePort.createInternalProcess(internal, responseBd);
        this.diagnosisPatientPersistencePort.patchCycles(body.getCycleNumber(), body.getCycleDay(), body.getDiagnosisPatient());
        return response;
    }

    private static DiagnosisOrderStageCreateResourceDto getDiagnosisOrderStageCreateResourceDto(DiagnosisOrderStageCreateResourceUseCaseDto body) {
        return DiagnosisOrderStageCreateResourceDto.builder()
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .diagnosisPatient(body.getDiagnosisPatient())
                .cycleDay(body.getCycleDay())
                .cycleNumber(body.getCycleNumber())
                .patientIdentification(body.getPatientIdentification())
                .patient(body.getPatient())
                .productionDate(body.getProductionDate())
                .status(StateMachineEnum.CHECK_PATIENT_OK)
                .build();
    }
}
