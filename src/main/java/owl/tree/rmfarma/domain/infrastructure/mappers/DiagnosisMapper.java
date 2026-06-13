package owl.tree.rmfarma.domain.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisCreateDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisUpdateResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;

@Mapper(componentModel = "spring")
public interface DiagnosisMapper {

    DiagnosisResourceDto toDiagnosisResourceDto(Diagnosis diagnosis);

    default Diagnosis toDiagnosisEntity(DiagnosisCreateDto dto) {
        if (dto == null) return null;
        return Diagnosis.builder()
                .code(dto.getCode())
                .description(dto.getDescription())
                .grpGroup(dto.getGrpGroup())
                .enabled(Boolean.TRUE)
                .build();
    }

    default Diagnosis toUpdateEntity(Diagnosis existing, DiagnosisUpdateResourceDto dto) {
        if (dto == null) return existing;
        if (dto.getCode() != null) existing.setCode(dto.getCode());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getGrpGroup() != null) existing.setGrpGroup(dto.getGrpGroup());
        return existing;
    }
}
