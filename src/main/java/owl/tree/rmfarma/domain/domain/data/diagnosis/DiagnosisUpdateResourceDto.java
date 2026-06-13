package owl.tree.rmfarma.domain.domain.data.diagnosis;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DiagnosisUpdateResourceDto {
    private String code;
    private String description;
    private String grpGroup;
}
