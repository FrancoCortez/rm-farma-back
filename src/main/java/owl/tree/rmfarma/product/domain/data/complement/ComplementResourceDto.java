package owl.tree.rmfarma.product.domain.data.complement;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ComplementResourceDto {
    private String id;
    private String code;
    private String description;
}
