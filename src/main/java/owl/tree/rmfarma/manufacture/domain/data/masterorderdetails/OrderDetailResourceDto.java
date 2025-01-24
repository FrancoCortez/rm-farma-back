package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.*;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailResourceDto;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailResourceDto {
    private String id;
    private String masterRecord;
    private String productName;
    private String productCode;
    private Integer quantity;
    private String unitMetric;
    private Integer volumeTotal;
    private String administrationTime;
    private String prot;
    private String condition;
    private String observation;
    private String complementName;
    private String complementCode;
    private String complementPart;
    private OffsetDateTime productionDate;
    private OffsetDateTime expirationDate;
    private ViaResourceDto via;
    private Set<CommercialOrderDetailResourceDto> commercialOrderDetails;
}
