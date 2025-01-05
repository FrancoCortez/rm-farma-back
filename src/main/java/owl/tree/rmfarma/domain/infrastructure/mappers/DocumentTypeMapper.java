package owl.tree.rmfarma.domain.infrastructure.mappers;

import org.mapstruct.Mapper;
import owl.tree.rmfarma.domain.domain.data.documentype.DocumentTypeResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.DocumentType;

@Mapper(componentModel = "spring")
public interface DocumentTypeMapper {

    DocumentTypeResourceDto toDocumentTypeResourceDto(DocumentType documentType);
}
