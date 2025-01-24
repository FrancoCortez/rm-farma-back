package owl.tree.rmfarma.service.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.api.ServiceServicePort;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServicePortImpl implements ServiceServicePort {
    private final ServicesPersistencePort servicesPersistencePort;

    @Override
    public List<ServiceResourceDto> findAll() {
        return this.servicesPersistencePort.findAll();
    }
}
