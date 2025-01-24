package owl.tree.rmfarma.service.domain.ports.api;

import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;

import java.util.List;

public interface ServiceServicePort {
    List<ServiceResourceDto> findAll();
}
