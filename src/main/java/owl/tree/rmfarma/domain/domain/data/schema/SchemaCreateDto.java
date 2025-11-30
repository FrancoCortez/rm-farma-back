package owl.tree.rmfarma.domain.domain.data.schema;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SchemaCreateDto {
    private String code;
    private String description;
}
