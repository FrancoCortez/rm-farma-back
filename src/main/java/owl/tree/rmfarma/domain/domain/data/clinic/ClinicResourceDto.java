package owl.tree.rmfarma.domain.domain.data.clinic;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
