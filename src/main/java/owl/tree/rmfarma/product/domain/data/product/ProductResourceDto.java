package owl.tree.rmfarma.product.domain.data.product;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductResourceDto {

    private String id;
    private String code;
    private String description;
    private String laboratory;
}
