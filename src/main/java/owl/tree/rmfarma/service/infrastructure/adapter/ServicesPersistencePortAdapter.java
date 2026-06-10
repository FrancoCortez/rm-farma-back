package owl.tree.rmfarma.service.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.service.infrastructure.repository.ServicesRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServicesPersistencePortAdapter implements ServicesPersistencePort {

    private final ServicesRepository servicesRepository;
    private final ServicesMapper servicesMapper;

    @Override
    public Optional<Services> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return this.servicesRepository.findByCodeAndEnabledTrue(code);
    }

    @Override
    public Optional<ServiceResourceDto> findResourceByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return this.servicesRepository.findByCodeAndEnabledTrue(code)
                .map(this.servicesMapper::toServiceResourceDto);
    }

    @Override
    public List<ServiceResourceDto> findAll() {
        return this.servicesRepository
                .findAllByEnabledTrue()
                .stream()
                .map(this.servicesMapper::toServiceResourceDto)
                .toList();
    }

    @Override
    public Services save(Services entity) {
        return this.servicesRepository.save(entity);
    }

    @Override
    public Optional<Services> findEnabledByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return this.servicesRepository.findByCodeAndEnabledTrue(code);
    }

    @Override
    public void disableByCode(String code) {
        this.servicesRepository.findByCodeAndEnabledTrue(code)
                .ifPresent(entity -> {
                    entity.setEnabled(false);
                    this.servicesRepository.save(entity);
                });
    }

    @Override
    public boolean existsByCode(String code) {
        return this.servicesRepository.existsByCode(code);
    }

    @Override
    public boolean existsByDescription(String description) {
        return this.servicesRepository.existsByDescription(description);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, String id) {
        return this.servicesRepository.existsByCodeAndIdNot(code, id);
    }

    @Override
    public boolean existsByDescriptionAndIdNot(String description, String id) {
        return this.servicesRepository.existsByDescriptionAndIdNot(description, id);
    }
}
