package owl.tree.rmfarma.product.domain.ports.api;

import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;

import java.util.List;

public interface ComplementServicePort {
    List<ComplementResourceDto> findAll();
}
