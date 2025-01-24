package owl.tree.rmfarma.manufacture.domain.data.masterorder;

import lombok.*;
import owl.tree.rmfarma.manufacture.application.masterorder.data.MasterOrderDetailsCreateResourceUseCaseDto;
import owl.tree.rmfarma.shared.enumes.StateMachineEnum;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InternalMasterOrderCreateResourceDto {
    private String patientIdentification;
    private OffsetDateTime productionDate;
    private StateMachineEnum status;
}
