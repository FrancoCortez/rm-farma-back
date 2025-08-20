package owl.tree.rmfarma.product.domain.data.commercialproduct;


import lombok.*;

import java.util.List;

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
    private Double concentration;
    private String concentrationUnit;
    private String defaultConcentration;
    private List<CommercialProductFactorResourceDto> factors;
}
