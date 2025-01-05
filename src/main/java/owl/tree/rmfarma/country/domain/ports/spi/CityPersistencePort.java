package owl.tree.rmfarma.country.domain.ports.spi;

import owl.tree.rmfarma.country.domain.data.city.CityResourceDto;

public interface CityPersistencePort {

    CityResourceDto findById(String id);
}
