package owl.tree.rmfarma.country.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "city")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "name", nullable = false, length = 50) private String name;
    @Column(name = "code", nullable = false, unique = true, length = 50) private String code;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @OneToMany(mappedBy = "city", orphanRemoval = true)
    private Set<Patient> patients = new LinkedHashSet<>();

}
