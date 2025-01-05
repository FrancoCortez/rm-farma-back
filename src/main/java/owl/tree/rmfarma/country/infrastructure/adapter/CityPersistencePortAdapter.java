package owl.tree.rmfarma.country.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.country.domain.data.city.CityResourceDto;
import owl.tree.rmfarma.country.domain.ports.spi.CityPersistencePort;
import owl.tree.rmfarma.country.infrastructure.entities.City;
import owl.tree.rmfarma.country.infrastructure.mappers.CityMapper;
import owl.tree.rmfarma.country.infrastructure.repository.CityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CityPersistencePortAdapter implements CityPersistencePort {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityResourceDto findById(String id) {
        if (id == null || id.isEmpty()) return null;
        City city = this.cityRepository.findById(id).orElse(null);
        if (city == null) return null;
        return this.cityMapper.toCityResourceDto(city);
    }
}
