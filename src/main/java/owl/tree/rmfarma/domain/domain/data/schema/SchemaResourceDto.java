package owl.tree.rmfarma.domain.domain.data.schema;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SchemaResourceDto {
    private String id;
    private String code;
    private String description;
}
