package owl.tree.rmfarma.domain.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaCreateDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.SchemaServicePort;
import owl.tree.rmfarma.domain.domain.ports.spi.SchemaPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchemaServiceImpl implements SchemaServicePort {
    private final SchemaPersistencePort schemaPersistencePort;

    public List<SchemaResourceDto> findAll() {
        return schemaPersistencePort.findAll();
    }

    public SchemaResourceDto createSchema (SchemaCreateDto dto) {
        SchemaResourceDto getByCode = this.schemaPersistencePort.findByCode(dto.getCode());
        if(getByCode != null) {
            throw new ExistsException(getByCode.getDescription(), "Esquema", getByCode.getCode());
        }
        return this.schemaPersistencePort.createSchema(dto);
    }
}
