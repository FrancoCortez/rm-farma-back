package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.*;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailResourceDto;
import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;

import java.time.OffsetDateTime;
import java.util.List;
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
    private Double quantity;
    private String unitMetric;
    private Double volumeTotal;
    private String administrationTime;
    private String prot;
    private String condition;
    private String observation;
    private String complementName;
    private String complementCode;
    private OffsetDateTime productionDate;
    private OffsetDateTime expirationDate;
    private String concentration;
    private ViaResourceDto via;
    private ComplementResourceDto complement;
    private List<CommercialOrderDetailResourceDto> commercialOrderDetails;
}
