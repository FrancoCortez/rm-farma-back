package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailUpdateDetailResourceDto {
    private String productCode;
    private String via;
    private Double dose;
    private OffsetDateTime productionDate;
    private OffsetDateTime expirationDate;
    private String unitMetric;
    private String complementCode;
    private Double volTotal;
    private String prot;
    private String condition;
    private String administrationTime;
    private String observation;
    private String concentration;
    private List<CommercialPartUpdateResourceDto> commercialPart;
}
