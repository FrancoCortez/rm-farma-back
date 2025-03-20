package owl.tree.rmfarma.domain.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.patient.infrastructure.entities.DiagnosisPatient;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "schema_de")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schema {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;
    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @OneToMany(mappedBy = "schema")
    private Set<DiagnosisPatient> diagnosisPatients = new LinkedHashSet<>();

}
