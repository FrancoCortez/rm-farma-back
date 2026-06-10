package owl.tree.rmfarma.service.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@Component
@RequiredArgsConstructor
public class UpdateServiceUseCase {

    private final ServicesPersistencePort servicesPersistencePort;
    private final ServicesMapper servicesMapper;

    public ServiceResourceDto update(String id, UpdateServiceRequest request) {
        String trimmedCode = request.code() == null ? null : request.code().trim();
        String trimmedDescription = request.description() == null ? null : request.description().trim();

        Services current = servicesPersistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException("Service", id));

        if (trimmedCode != null && !trimmedCode.equals(current.getCode())
                && servicesPersistencePort.existsByCodeAndIdNot(trimmedCode, current.getId())) {
            throw new ExistsException("code", "Service", trimmedCode);
        }
        if (trimmedDescription != null && !trimmedDescription.equals(current.getDescription())
                && servicesPersistencePort.existsByDescriptionAndIdNot(trimmedDescription, current.getId())) {
            throw new ExistsException("description", "Service", trimmedDescription);
        }

        current.setCode(trimmedCode);
        current.setDescription(trimmedDescription);
        Services persisted = servicesPersistencePort.save(current);
        return servicesMapper.toServiceResourceDto(persisted);
    }
}
