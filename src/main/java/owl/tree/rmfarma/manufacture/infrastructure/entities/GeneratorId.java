package owl.tree.rmfarma.manufacture.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import owl.tree.rmfarma.shared.entities.BaseEntity;

@Entity(name = "generator_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
@EntityListeners(AuditingEntityListener.class)
public class GeneratorId extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "correlative", nullable = false)
    private Integer correlative;
    @Column(name = "year", nullable = false)
    private Integer year;
}
