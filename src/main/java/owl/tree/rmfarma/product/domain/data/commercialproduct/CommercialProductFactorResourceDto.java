package owl.tree.rmfarma.product.domain.data.commercialproduct;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommercialProductFactorResourceDto {
    private String id;
    private String administrationRoute;
    private Double factor;
}
