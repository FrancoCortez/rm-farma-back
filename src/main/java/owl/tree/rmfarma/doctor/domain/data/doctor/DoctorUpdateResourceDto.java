package owl.tree.rmfarma.doctor.domain.data.doctor;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DoctorUpdateResourceDto {
    private String rut;
    private String name;
}
