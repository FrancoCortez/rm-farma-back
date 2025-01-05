package owl.tree.rmfarma.manufacture.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import owl.tree.rmfarma.country.infrastructure.entities.Province;
import owl.tree.rmfarma.product.infrastructure.entities.Product;

@Entity(name = "order_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "product_name", nullable = false, length = 50) private String productName;
    @Column(name = "product_code", nullable = false, length = 30) private String productCode;
    @Column(name = "product_laboratory", nullable = false, length = 50) private String productLaboratory;
    @Column(name = "part", nullable = false, length = 50) private String part;
    @Column(name = "quantity", nullable = false) private Integer quantity;
    @Column(name = "batch", nullable = false, length = 50) private String batch;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "master_order_id")
    private MasterOrder masterOrder;

}
