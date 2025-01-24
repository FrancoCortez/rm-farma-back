package owl.tree.rmfarma.doctor.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.patient.infrastructure.entities.DiagnosisPatient;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_doctor_rut", columnList = "rut"),
        @Index(name = "idx_doctor_code", columnList = "code")
})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "code", unique = true, nullable = false)
    private Integer code;
    @Column(name = "rut", length = 30)
    private String rut;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @OneToMany(mappedBy = "doctor", orphanRemoval = true)
    private Set<DiagnosisPatient> diagnosisPatients = new LinkedHashSet<>();

}
