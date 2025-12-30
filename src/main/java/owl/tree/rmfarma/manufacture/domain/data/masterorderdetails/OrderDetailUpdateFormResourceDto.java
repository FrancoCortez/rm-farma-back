package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailUpdateFormResourceDto {
    private String master;
    private String masterRecord;
    private OrderDetailUpdateDetailResourceDto details;

}
