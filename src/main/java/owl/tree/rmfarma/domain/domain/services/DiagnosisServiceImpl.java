package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.DiagnosisServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.DiagnosisPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisServicePort {

    private final DiagnosisPersistencePort diagnosisPersistencePort;

    public List<DiagnosisResourceDto> findAll() {
        return diagnosisPersistencePort.findAll();
    }
}
