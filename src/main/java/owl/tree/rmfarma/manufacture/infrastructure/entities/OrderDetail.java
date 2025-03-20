package owl.tree.rmfarma.manufacture.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;
import owl.tree.rmfarma.product.infrastructure.entities.Complement;
import owl.tree.rmfarma.product.infrastructure.entities.Product;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "order_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "master_record", nullable = false, length = 30)
    private String masterRecord; // check
    @Column(name = "product_name", nullable = false, length = 50)
    private String productName;
    @Column(name = "product_code", nullable = false, length = 30)
    private String productCode; // check
    @Column(name = "product_laboratory", length = 50)
    private String productLaboratory; // check
    @Column(name = "quantity", nullable = false)
    private Integer quantity; // check
    @Column(name = "complement_name", nullable = false, length = 50)
    private String complementName; // check
    @Column(name = "complement_code", nullable = false, length = 30)
    private String complementCode; // check
    @Column(name = "via_code",  length = 30)
    private String viaCode; // check
    @Column(name = "via_description",  length = 100)
    private String viaDescription; // check
    @Column(name = "pharmaceutical_chemist",  length = 100)
    private String pharmaceuticalChemist;
    @Column(name = "prot", length = 5)
    private String prot;
    @Column(name = "volume_total")
    private Integer volumeTotal;
    @Column(name = "unit_metric", length = 10)
    private String unitMetric;
    @Column(name = "production_date")
    private OffsetDateTime productionDate;
    @Column(name = "expiration_date_date")
    private OffsetDateTime expirationDate;

    @Column(name = "conditional", length = 50)
    private String condition;
    @Column(name = "administration_time", length = 50)
    private String administrationTime;
    @Column(name = "observation", length = 500)
    private String observation;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "master_order_id")
    private MasterOrder masterOrder;

    @ManyToOne
    @JoinColumn(name = "complement_id")
    private Complement complement;

    @ManyToOne
    @JoinColumn(name = "via_id")
    private Via via;

    @OneToMany(mappedBy = "orderDetail")
    private Set<CommercialOrderDetail> commercialOrderDetails = new LinkedHashSet<>();

}
