package owl.tree.rmfarma.manufacture.application.masterorder.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    @Max(value = 500, message = "quantity must be at most 500")
    private Integer quantity;
}
