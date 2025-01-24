package owl.tree.rmfarma.domain.domain.data.hospitalunit;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HospitalUnitResourceDto {
    private String id;
    private String code;
    private String description;
}
