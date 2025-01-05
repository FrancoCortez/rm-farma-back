package owl.tree.rmfarma.domain.domain.ports.api;

import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;

import java.util.List;

public interface IsapreServicePort {
    List<IsapreResourceDto> findAll();
}
