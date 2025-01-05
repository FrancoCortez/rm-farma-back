package owl.tree.rmfarma.country.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "province")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "name", nullable = false, length = 50) private String name;
    @Column(name = "code", nullable = false, unique = true, length = 50) private String code;


    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "province", orphanRemoval = true)
    private Set<City> cities = new LinkedHashSet<>();

}
