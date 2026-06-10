package owl.tree.rmfarma.service.domain.ports.api;

import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;

import java.util.List;

public interface ServiceServicePort {
    List<ServiceResourceDto> findAll();

    ServiceResourceDto create(CreateServiceRequest request);

    ServiceResourceDto update(String code, UpdateServiceRequest request);

    void deleteByCode(String code);

    ServiceResourceDto findByCode(String code);
}
