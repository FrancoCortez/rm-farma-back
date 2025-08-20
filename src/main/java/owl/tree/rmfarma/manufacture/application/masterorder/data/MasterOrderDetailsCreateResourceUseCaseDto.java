package owl.tree.rmfarma.manufacture.application.masterorder.data;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MasterOrderDetailsCreateResourceUseCaseDto {
    private String productCode;
    private Integer dose;
    private String unitMetric;
    private String complementCode;
    private Integer volTotal;
    private String prot;
    private String condition;
    private String administrationTime;
    private Integer realPart;
    private String observation;
    private OffsetDateTime productionDate;
    private OffsetDateTime expirationDate;
    private String concentration;
    private List<CommercialAddCreateResourceUseCaseDto> commercialPart;
}
