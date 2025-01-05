package owl.tree.rmfarma.patient.application.patient.data;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PatientCreateResourceUseCaseDto {
    private String rut;
    private String identification;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String city;
    private String villa;
    private String street;
    private Integer houseNumber;
    private OffsetDateTime dateOfBirth;
    private String doctorRut;
    private String doctorName;
    private String doctorLastName;
    private String doctorPhone;
    private String doctorEmail;
    private Integer cycleNumber;
    private Integer cycleDay;
    private String clinic;
    private String schema;
    private String isapre;
    private String diagnosis;
    private String services;
}
