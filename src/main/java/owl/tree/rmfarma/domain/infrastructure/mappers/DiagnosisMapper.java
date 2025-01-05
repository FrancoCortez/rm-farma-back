package owl.tree.rmfarma.domain.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.Clinic;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;

@Mapper(componentModel = "spring")
public interface DiagnosisMapper {

    DiagnosisResourceDto toDiagnosisResourceDto(Diagnosis diagnosis);
}
