package owl.tree.rmfarma.service.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "code", unique = true, nullable = false, length = 30) private String code;
    @Column(name = "description", nullable = false, length = 100) private String description;

    @OneToMany(mappedBy = "services", orphanRemoval = true)
    private Set<Patient> patients = new LinkedHashSet<>();

}
