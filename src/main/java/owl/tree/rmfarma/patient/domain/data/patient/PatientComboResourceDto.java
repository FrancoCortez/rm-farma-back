package owl.tree.rmfarma.patient.domain.data.patient;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PatientComboResourceDto {
    private String identification;
    private String label;
}
