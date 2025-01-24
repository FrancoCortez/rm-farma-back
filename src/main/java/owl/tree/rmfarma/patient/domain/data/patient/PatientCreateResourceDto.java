package owl.tree.rmfarma.patient.domain.data.patient;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PatientCreateResourceDto {
    private String id;
    private String rut;
    private String type;
    private String identification;
    private String name;
    private String lastName;
    private String isapre;
}
