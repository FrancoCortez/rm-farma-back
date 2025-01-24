package owl.tree.rmfarma.doctor.domain.data.doctor;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DoctorResourceDto {
    private String id;
    private String rut;
    private String name;
    private Integer code;
}
