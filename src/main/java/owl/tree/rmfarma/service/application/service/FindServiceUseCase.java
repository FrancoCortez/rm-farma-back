package owl.tree.rmfarma.service.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.api.ServiceServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindServiceUseCase {
    private final ServiceServicePort serviceServicePort;

    public List<ServiceResourceDto> findAll() {
        return this.serviceServicePort.findAll();
    }
}
