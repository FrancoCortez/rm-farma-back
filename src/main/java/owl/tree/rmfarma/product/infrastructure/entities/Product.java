package owl.tree.rmfarma.product.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;
import owl.tree.rmfarma.shared.entities.BaseEntity;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "code", unique = true, nullable = false, length = 30)
    private String code;
    @Column(name = "description", nullable = false, length = 100)
    private String description;
//    @Column(name = "laboratory", nullable = false, length = 50)
//    private String laboratory;

    @OneToMany(mappedBy = "product")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private Set<CommercialProduct> commercialProducts = new LinkedHashSet<>();

}
