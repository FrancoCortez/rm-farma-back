package owl.tree.rmfarma.service.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;
import owl.tree.rmfarma.service.domain.ports.api.ServiceServicePort;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceServicePortImpl implements ServiceServicePort {
    private final ServicesPersistencePort servicesPersistencePort;
    private final ServicesMapper servicesMapper;

    @Override
    public List<ServiceResourceDto> findAll() {
        return this.servicesPersistencePort.findAll();
    }

    @Override
    public ServiceResourceDto create(CreateServiceRequest request) {
        String trimmedCode = request.code() == null ? null : request.code().trim();
        String trimmedDescription = request.description() == null ? null : request.description().trim();
        if (servicesPersistencePort.existsByCode(trimmedCode)) {
            throw new owl.tree.rmfarma.shared.exception.domain.ExistsException("code", "Service", trimmedCode);
        }
        if (servicesPersistencePort.existsByDescription(trimmedDescription)) {
            throw new owl.tree.rmfarma.shared.exception.domain.ExistsException("description", "Service", trimmedDescription);
        }
        Services entity = servicesMapper.toServices(
                new CreateServiceRequest(trimmedCode, trimmedDescription));
        entity.setEnabled(Boolean.TRUE);
        Services persisted = servicesPersistencePort.save(entity);
        return servicesMapper.toServiceResourceDto(persisted);
    }

    @Override
    public ServiceResourceDto update(String id, UpdateServiceRequest request) {
        String trimmedCode = request.code() == null ? null : request.code().trim();
        String trimmedDescription = request.description() == null ? null : request.description().trim();
        Services current = servicesPersistencePort.findById(id)
                .orElseThrow(() -> new owl.tree.rmfarma.shared.exception.domain.NotFoundException("Service", id));
        if (trimmedCode != null && !trimmedCode.equals(current.getCode())
                && servicesPersistencePort.existsByCodeAndIdNot(trimmedCode, current.getId())) {
            throw new owl.tree.rmfarma.shared.exception.domain.ExistsException("code", "Service", trimmedCode);
        }
        if (trimmedDescription != null && !trimmedDescription.equals(current.getDescription())
                && servicesPersistencePort.existsByDescriptionAndIdNot(trimmedDescription, current.getId())) {
            throw new owl.tree.rmfarma.shared.exception.domain.ExistsException("description", "Service", trimmedDescription);
        }
        current.setCode(trimmedCode);
        current.setDescription(trimmedDescription);
        return servicesMapper.toServiceResourceDto(servicesPersistencePort.save(current));
    }

    @Override
    public void deleteById(String id) {
        Optional<Services> found = servicesPersistencePort.findEnabledById(id);
        if (found.isEmpty()) {
            throw new owl.tree.rmfarma.shared.exception.domain.NotFoundException("Service", id);
        }
        servicesPersistencePort.disableById(id);
    }

    @Override
    public ServiceResourceDto findById(String id) {
        return servicesPersistencePort.findById(id)
                .map(servicesMapper::toServiceResourceDto)
                .orElseThrow(() -> new owl.tree.rmfarma.shared.exception.domain.NotFoundException("Service", id));
    }
}
