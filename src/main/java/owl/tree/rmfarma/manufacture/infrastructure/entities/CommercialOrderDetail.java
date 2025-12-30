package owl.tree.rmfarma.manufacture.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import owl.tree.rmfarma.product.infrastructure.entities.CommercialProduct;
import owl.tree.rmfarma.shared.entities.BaseEntity;

@Entity(name = "commercial_order_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@EntityListeners(AuditingEntityListener.class)
public class CommercialOrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "commercial", length = 100)
    private String commercial;
    @Column(name = "code", length = 50)
    private String code;
    @Column(name = "laboratory", length = 100)
    private String laboratory;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "batch", length = 50)
    private String batch;

    @ManyToOne
    @JoinColumn(name = "commercial_product_id")
    private CommercialProduct commercialProduct;

    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

}
