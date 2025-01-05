package owl.tree.rmfarma.doctor.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_doctor_rut", columnList = "rut")
})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "rut", unique = true, nullable = false, length = 30) private String rut;
    @Column(name = "name", nullable = false, length = 50) private String name;
    @Column(name = "last_name", nullable = false, length = 50) private String lastName;
    @Column(name = "phone", length = 20) private String phone;
    @Column(name = "email", length = 100) private String email;

    @OneToMany(mappedBy = "doctor", orphanRemoval = true)
    private Set<Patient> patients = new LinkedHashSet<>();

}
