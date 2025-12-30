package owl.tree.rmfarma.product.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import owl.tree.rmfarma.shared.entities.BaseEntity;

@Entity(name = "commercial_product_factor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Audited
public class CommercialProductFactor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "commercial_product_id", nullable = false)
    private CommercialProduct commercialProduct;

    @Column(name = "administration_route", length = 50, nullable = false)
    private String administrationRoute;

    @Column(name = "factor", nullable = false)
    private Double factor;
}
