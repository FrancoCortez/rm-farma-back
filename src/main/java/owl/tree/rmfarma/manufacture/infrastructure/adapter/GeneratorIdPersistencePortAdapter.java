package owl.tree.rmfarma.manufacture.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.generateid.GeneratorIdCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.generateid.GeneratorIdResourceDto;
import owl.tree.rmfarma.manufacture.domain.ports.spi.GeneratorIdPersistencePort;
import owl.tree.rmfarma.manufacture.infrastructure.entities.GeneratorId;
import owl.tree.rmfarma.manufacture.infrastructure.mappers.GeneratorIdMapper;
import owl.tree.rmfarma.manufacture.infrastructure.repository.GeneratorIdRepository;

@Component
@RequiredArgsConstructor
public class GeneratorIdPersistencePortAdapter implements GeneratorIdPersistencePort {
    private final GeneratorIdRepository generatorIdRepository;
    private final GeneratorIdMapper generatorIdMapper;


    public GeneratorIdResourceDto generateId(Integer year) {
        GeneratorIdResourceDto generateId = this.generatorIdMapper.toGenerateIdResourceDto(this.generatorIdRepository.findTopByYearOrderByCorrelativeDesc(year).orElse(null));
        GeneratorIdResourceDto result = new GeneratorIdResourceDto();
        if (generateId == null) {
            result.setYear(year);
            result.setCorrelative(1);
        } else {
            result.setYear(year);
            result.setCorrelative(generateId.getCorrelative() + 1);
        }
        GeneratorId entity = this.generatorIdMapper.toGeneratorId(GeneratorIdCreateResourceDto.builder()
                .correlative(result.getCorrelative())
                .year(result.getYear())
                .build());
        this.generatorIdRepository.save(entity);
        return result;
    }
}
