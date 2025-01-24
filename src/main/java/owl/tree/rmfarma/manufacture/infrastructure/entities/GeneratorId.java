package owl.tree.rmfarma.manufacture.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "generator_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorId {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "correlative", nullable = false)
    private Integer correlative;
    @Column(name = "year", nullable = false)
    private Integer year;
}
