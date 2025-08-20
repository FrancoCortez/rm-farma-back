package owl.tree.rmfarma.product.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.manufacture.infrastructure.entities.CommercialOrderDetail;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "commercial_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommercialProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "code", unique = true, nullable = false, length = 30)
    private String code;
    @Column(name = "description", nullable = false, length = 100)
    private String description;
    @Column(name = "laboratory", nullable = false, length = 50)
    private String laboratory;
    @Column(name = "concentration")
    private Double concentration;
    @Column(name = "concentration_unit", length = 20)
    private String concentrationUnit;
    @Column(name = "default_concentration", length = 30)
    private String defaultConcentration;

    @OneToMany(mappedBy = "commercialProduct")
    private Set<CommercialOrderDetail> commercialOrderDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "commercialProduct")
    private Set<CommercialProductFactor> factors = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
