package owl.tree.rmfarma.service.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.service.infrastructure.respository.ServicesRepository;

@Component
@RequiredArgsConstructor
public class ServicesPersistencePortAdapter implements ServicesPersistencePort {

    private final ServicesRepository servicesRepository;
    private final ServicesMapper servicesMapper;

    public ServiceResourceDto findByCode(String code) {
        if (code == null || code.isEmpty()) return null;
        Services services = this.servicesRepository.findByCode(code).orElse(null);
        if(services == null) return null;
        return this.servicesMapper.toServiceResourceDto(services);
    }
}
