package owl.tree.rmfarma.domain.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;
import owl.tree.rmfarma.shared.entities.BaseEntity;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "via")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Via extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;
    @Column(name = "description", nullable = false, length = 100)
    private String description;
    @OneToMany(mappedBy = "via")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

}
