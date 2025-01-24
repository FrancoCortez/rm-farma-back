package owl.tree.rmfarma.manufacture.application.masterorder.data;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MasterOrderCreateResourceUseCaseDto {
    private String patientIdentification;
    private String via;
    private String diagnosisOrder;
    private String master;
    private MasterOrderDetailsCreateResourceUseCaseDto details;
}
