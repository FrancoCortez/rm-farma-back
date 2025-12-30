package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;


import lombok.*;
import owl.tree.rmfarma.shared.enumes.StateMachineOrderDetailsEnum;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailCreateResourceDto {
    private String masterRecord;
    private String productName;
    private String productCode;
    private String productLaboratory;
    private Double quantity;
    private String complementName;
    private String complementCode;
    private String product;
    private String masterOrder;
    private String complement;
    private String viaCode;
    private String viaDescription;
    private String via;
    private String prot;
    private Double volumeTotal;
    private String unitMetric;
    private String condition;
    private String administrationTime;
    private StateMachineOrderDetailsEnum status;
    private String observation;
    private OffsetDateTime productionDate;
    private OffsetDateTime expirationDate;
    private OffsetDateTime administrationDate;
    private String bedDay;
    private String concentration;
}
