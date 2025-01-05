package owl.tree.rmfarma.domain.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
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
        return this.diagnosisRepository.findAll()
                .stream()
                .map(this.diagnosisMapper::toDiagnosisResourceDto)
                .toList();
    }

    public DiagnosisResourceDto findByCode(String code) {
        if(code == null || code.isEmpty()) return null;
        Diagnosis diagnosis = this.diagnosisRepository.findByCode(code).orElse(null);
        if (diagnosis == null) return null;
        return this.diagnosisMapper.toDiagnosisResourceDto(diagnosis);
    }
}
