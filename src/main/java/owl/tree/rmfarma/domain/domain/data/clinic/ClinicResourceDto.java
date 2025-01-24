package owl.tree.rmfarma.domain.domain.data.clinic;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ClinicResourceDto {
    private String id;
    private String code;
    private String description;
}
