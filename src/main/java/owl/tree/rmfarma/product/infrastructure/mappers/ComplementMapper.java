package owl.tree.rmfarma.product.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;
import owl.tree.rmfarma.product.infrastructure.entities.Complement;

@Mapper(componentModel = "spring")
public interface ComplementMapper {

    ComplementResourceDto toComplementResourceDto(Complement entity);
}
