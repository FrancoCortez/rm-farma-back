package owl.tree.rmfarma.domain.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "diagnosis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "code", nullable = false, unique = true, length = 50) private String code;
    @Column(name = "description", nullable = false, length = 100) private String description;
    @Column(name = "grp_group", nullable = false, length = 30) private String grpGroup;

    @OneToMany(mappedBy = "diagnosis", orphanRemoval = true)
    private Set<Patient> patients = new LinkedHashSet<>();

}
