package owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail;

import lombok.*;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommercialOrderDetailResourceDto {
    private String id;
    private String commercial;
    private String code;
    private String laboratory;
    private Integer quantity;
    private String batch;
    private CommercialProductResourceDto commercialProduct;
}
