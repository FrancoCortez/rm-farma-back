package owl.tree.rmfarma.manufacture.domain.ports.api;

import owl.tree.rmfarma.manufacture.application.masterorder.data.MasterOrderCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.InternalMasterOrderCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface MasterOrderServicePort {
    List<MasterOrderResourceDto> findAll(OffsetDateTime searchDay, String searchIdentification);
    List<MasterOrderResourceDto> findHistoryByPatientIdentification(String identification, String diagnosisId);
    MasterOrderResourceDto create(MasterOrderCreateResourceUseCaseDto masterOrderCreateResourceUseCaseDto);
    MasterOrderResourceDto createInternalProcess (InternalMasterOrderCreateResourceDto masterOrderCreateResourceDto, DiagnosisOrderStageResourceDto response);
}
