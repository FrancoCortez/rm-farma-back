package owl.tree.rmfarma.product.domain.data.commercialproduct;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommercialProductResourceDto {
    private String id;
    private String code;
    private String description;
    private String laboratory;
}
