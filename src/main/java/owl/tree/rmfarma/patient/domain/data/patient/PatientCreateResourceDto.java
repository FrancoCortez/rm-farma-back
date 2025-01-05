package owl.tree.rmfarma.patient.domain.data.patient;
import lombok.*;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PatientCreateResourceDto {
    private String rut;
    private String identification;
    private String name;
    private String lastName;
    private String villa;
    private String street;
    private Integer houseNumber;
    private OffsetDateTime dateOfBirth;
    private String phone;
    private String email;
    private Integer cycleNumber;
    private Integer cycleDay;

    private String city;
    private String doctor;
    private String services;
    private String diagnosis;
    private String clinic;
    private String isapre;
    private String schema;
}
