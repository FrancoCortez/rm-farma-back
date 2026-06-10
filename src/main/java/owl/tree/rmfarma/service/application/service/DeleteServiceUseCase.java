package owl.tree.rmfarma.service.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@Component
@RequiredArgsConstructor
public class DeleteServiceUseCase {

    private final ServicesPersistencePort servicesPersistencePort;

    public void deleteByCode(String code) {
        if (servicesPersistencePort.findEnabledByCode(code).isEmpty()) {
            throw new NotFoundException("Service", code);
        }
        servicesPersistencePort.disableByCode(code);
    }
}
