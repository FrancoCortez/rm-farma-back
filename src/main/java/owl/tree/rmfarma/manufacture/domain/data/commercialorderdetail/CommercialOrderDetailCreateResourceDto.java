package owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommercialOrderDetailCreateResourceDto {
    private String commercial;
    private String code;
    private String laboratory;
    private Integer quantity;
    private String batch;
    private String commercialProduct;
    private String orderDetail;
}
