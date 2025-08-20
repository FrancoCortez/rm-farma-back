package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;


import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailUpdateResourceDto {
    private String id;
    private String masterRecord;
    private String productName;
    private String productCode;
    private String productLaboratory;
    private Integer quantity;
    private String complementName;
    private String complementCode;
    private String product;
    private String masterOrder;
    private String complement;
    private String viaCode;
    private String viaDescription;
    private String via;
    private String prot;
    private Integer volumeTotal;
    private String unitMetric;
    private String condition;
    private String administrationTime;
    private String observation;
    private String concentration;
    private OffsetDateTime productionDate;
    private OffsetDateTime expirationDate;
}
