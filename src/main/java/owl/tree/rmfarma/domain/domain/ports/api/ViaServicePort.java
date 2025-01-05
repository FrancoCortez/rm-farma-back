package owl.tree.rmfarma.domain.domain.ports.api;

import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;

import java.util.List;

public interface ViaServicePort {
    List<ViaResourceDto> findAll();
}
