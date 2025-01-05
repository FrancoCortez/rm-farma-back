package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.SchemaServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.SchemaPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchemaServiceImpl implements SchemaServicePort {
    private final SchemaPersistencePort schemaPersistencePort;

    public List<SchemaResourceDto> findAll() {
        return schemaPersistencePort.findAll();
    }
}
