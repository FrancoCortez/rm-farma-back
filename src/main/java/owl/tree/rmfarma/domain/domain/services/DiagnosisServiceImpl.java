package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisCreateDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisUpdateResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.DiagnosisServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.DiagnosisPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;
import owl.tree.rmfarma.shared.exception.domain.IsEmptyException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisServicePort {

    private final DiagnosisPersistencePort diagnosisPersistencePort;

    public List<DiagnosisResourceDto> findAll() {
        return diagnosisPersistencePort.findAll();
    }

    @Override
    public DiagnosisResourceDto findById(String id) {
        if (id == null || id.isEmpty()) throw new IsEmptyException("id", "Diagnóstico");
        DiagnosisResourceDto result = this.diagnosisPersistencePort.findById(id);
        if (result == null) throw new NotFoundException("id", "Diagnóstico");
        return result;
    }

    @Override
    public DiagnosisResourceDto createDiagnosis(DiagnosisCreateDto dto) {
        if (dto.getCode() == null || dto.getCode().isEmpty()) throw new IsEmptyException("code", "Diagnóstico");
        if (dto.getDescription() == null || dto.getDescription().isEmpty()) throw new IsEmptyException("description", "Diagnóstico");
        DiagnosisResourceDto existingByCode = this.diagnosisPersistencePort.findByCodeIncludingDisabled(dto.getCode());
        if (existingByCode != null) throw new ExistsException(existingByCode.getDescription(), "Diagnóstico", existingByCode.getCode());
        DiagnosisResourceDto existingByDescription = this.findByDescriptionIncludingDisabled(dto.getDescription());
        if (existingByDescription != null) throw new ExistsException(existingByDescription.getDescription(), "Diagnóstico descripción", existingByDescription.getDescription());
        return this.diagnosisPersistencePort.createDiagnosis(dto);
    }

    @Override
    public DiagnosisResourceDto updateDiagnosis(String id, DiagnosisUpdateResourceDto dto) {
        if (id == null || id.isEmpty()) throw new IsEmptyException("id", "Diagnóstico");
        DiagnosisResourceDto existing = this.diagnosisPersistencePort.findById(id);
        if (existing == null) throw new NotFoundException("id", "Diagnóstico");
        if (dto.getCode() != null && !dto.getCode().equals(existing.getCode())) {
            DiagnosisResourceDto dup = this.diagnosisPersistencePort.findByCodeIncludingDisabled(dto.getCode());
            if (dup != null && !dup.getId().equals(id)) throw new ExistsException(dup.getDescription(), "Diagnóstico", dup.getCode());
        }
        if (dto.getDescription() != null && !dto.getDescription().equals(existing.getDescription())) {
            DiagnosisResourceDto dup = this.findByDescriptionIncludingDisabled(dto.getDescription());
            if (dup != null && !dup.getId().equals(id)) throw new ExistsException(dup.getDescription(), "Diagnóstico descripción", dup.getDescription());
        }
        return this.diagnosisPersistencePort.updateDiagnosis(id, dto);
    }

    @Override
    public void softDelete(String id) {
        if (id == null || id.isEmpty()) throw new IsEmptyException("id", "Diagnóstico");
        DiagnosisResourceDto existing = this.diagnosisPersistencePort.findById(id);
        if (existing == null) throw new NotFoundException("id", "Diagnóstico");
        this.diagnosisPersistencePort.softDelete(id);
    }

    private DiagnosisResourceDto findByDescriptionIncludingDisabled(String description) {
        return this.diagnosisPersistencePort.findAll().stream()
                .filter(d -> d.getDescription() != null && d.getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElse(null);
    }
}
