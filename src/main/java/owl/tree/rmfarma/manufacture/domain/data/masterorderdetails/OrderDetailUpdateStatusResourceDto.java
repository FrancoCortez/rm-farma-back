package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailUpdateStatusResourceDto {
    private String masterRecord;
    private String code;
    private String reasonForSuspension;
}
