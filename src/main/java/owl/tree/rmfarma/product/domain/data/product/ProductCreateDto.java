package owl.tree.rmfarma.product.domain.data.product;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductCreateDto {
    private String code;
    private String description;
}
