package owl.tree.rmfarma.domain.domain.data.documentype;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DocumentTypeResourceDto {
    private String id;
    private String code;
    private String description;
}
