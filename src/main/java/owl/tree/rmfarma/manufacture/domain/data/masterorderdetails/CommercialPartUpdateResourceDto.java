package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommercialPartUpdateResourceDto {
    private String commercial;
    private String batch;
    private Integer part;
}
