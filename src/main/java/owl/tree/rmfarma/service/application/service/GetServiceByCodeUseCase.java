package owl.tree.rmfarma.service.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@Component
@RequiredArgsConstructor
public class GetServiceByCodeUseCase {

    private final ServicesPersistencePort servicesPersistencePort;
    private final ServicesMapper servicesMapper;

    public ServiceResourceDto findByCode(String code) {
        return servicesPersistencePort.findByCode(code)
                .map(servicesMapper::toServiceResourceDto)
                .orElseThrow(() -> new NotFoundException("Service", code));
    }
}
