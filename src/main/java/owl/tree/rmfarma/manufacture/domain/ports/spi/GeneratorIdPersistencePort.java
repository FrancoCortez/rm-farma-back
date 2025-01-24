package owl.tree.rmfarma.manufacture.domain.ports.spi;

import owl.tree.rmfarma.manufacture.domain.data.generateid.GeneratorIdResourceDto;

public interface GeneratorIdPersistencePort {

    GeneratorIdResourceDto generateId(Integer year);
}
