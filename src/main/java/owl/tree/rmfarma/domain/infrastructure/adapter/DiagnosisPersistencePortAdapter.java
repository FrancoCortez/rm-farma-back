package owl.tree.rmfarma.domain.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisCreateDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisUpdateResourceDto;
import owl.tree.rmfarma.domain.domain.ports.spi.DiagnosisPersistencePort;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;
import owl.tree.rmfarma.domain.infrastructure.mappers.DiagnosisMapper;
import owl.tree.rmfarma.domain.infrastructure.repository.DiagnosisRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiagnosisPersistencePortAdapter implements DiagnosisPersistencePort {

    private final DiagnosisRepository diagnosisRepository;
    private final DiagnosisMapper diagnosisMapper;

    public List<DiagnosisResourceDto> findAll() {
        return this.diagnosisRepository.findAllByEnabledTrue()
                .stream()
                .map(this.diagnosisMapper::toDiagnosisResourceDto)
                .toList();
    }

    public DiagnosisResourceDto findByCode(String code) {
        if (code == null || code.isEmpty()) return null;
        Diagnosis diagnosis = this.diagnosisRepository.findByCodeAndEnabledTrue(code).orElse(null);
        if (diagnosis == null) return null;
        return this.diagnosisMapper.toDiagnosisResourceDto(diagnosis);
    }

    @Override
    public DiagnosisResourceDto findByCodeIncludingDisabled(String code) {
        if (code == null || code.isEmpty()) return null;
        Diagnosis diagnosis = this.diagnosisRepository.findByCode(code).orElse(null);
        if (diagnosis == null) return null;
        return this.diagnosisMapper.toDiagnosisResourceDto(diagnosis);
    }

    @Override
    public DiagnosisResourceDto findById(String id) {
        if (id == null || id.isEmpty()) return null;
        Diagnosis diagnosis = this.diagnosisRepository.findByIdAndEnabledTrue(id).orElse(null);
        if (diagnosis == null) return null;
        return this.diagnosisMapper.toDiagnosisResourceDto(diagnosis);
    }

    @Override
    public DiagnosisResourceDto createDiagnosis(DiagnosisCreateDto dto) {
        return this.diagnosisMapper.toDiagnosisResourceDto(
                this.diagnosisRepository.save(this.diagnosisMapper.toDiagnosisEntity(dto))
        );
    }

    @Override
    public DiagnosisResourceDto updateDiagnosis(String id, DiagnosisUpdateResourceDto dto) {
        Diagnosis existing = this.diagnosisRepository.findById(id).orElse(null);
        if (existing == null) return null;
        Diagnosis updated = this.diagnosisMapper.toUpdateEntity(existing, dto);
        return this.diagnosisMapper.toDiagnosisResourceDto(this.diagnosisRepository.save(updated));
    }

    @Override
    public void softDelete(String id) {
        Diagnosis existing = this.diagnosisRepository.findById(id).orElse(null);
        if (existing == null) return;
        existing.setEnabled(Boolean.FALSE);
        this.diagnosisRepository.save(existing);
    }
}
