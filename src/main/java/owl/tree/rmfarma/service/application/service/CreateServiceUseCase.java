package owl.tree.rmfarma.service.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;

@Component
@RequiredArgsConstructor
public class CreateServiceUseCase {

    private final ServicesPersistencePort servicesPersistencePort;
    private final ServicesMapper servicesMapper;

    public ServiceResourceDto create(CreateServiceRequest request) {
        String trimmedCode = request.code() == null ? null : request.code().trim();
        String trimmedDescription = request.description() == null ? null : request.description().trim();

        if (servicesPersistencePort.existsByCode(trimmedCode)) {
            throw new ExistsException("code", "Service", trimmedCode);
        }
        if (servicesPersistencePort.existsByDescription(trimmedDescription)) {
            throw new ExistsException("description", "Service", trimmedDescription);
        }

        Services entity = servicesMapper.toServices(
                new CreateServiceRequest(trimmedCode, trimmedDescription));
        entity.setEnabled(Boolean.TRUE);
        Services persisted = servicesPersistencePort.save(entity);
        return servicesMapper.toServiceResourceDto(persisted);
    }
}
