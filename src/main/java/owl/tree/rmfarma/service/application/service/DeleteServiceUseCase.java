package owl.tree.rmfarma.service.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@Component
@RequiredArgsConstructor
public class DeleteServiceUseCase {

    private final ServicesPersistencePort servicesPersistencePort;

    public void deleteById(String id) {
        if (servicesPersistencePort.findEnabledById(id).isEmpty()) {
            throw new NotFoundException("Service", id);
        }
        servicesPersistencePort.disableById(id);
    }
}
