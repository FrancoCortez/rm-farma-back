package owl.tree.rmfarma.domain.domain.data.diagnosis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DiagnosisCreateDto {
    @NotBlank(message = "code is required")
    private String code;

    @NotBlank(message = "description is required")
    private String description;

    private String grpGroup;
}
