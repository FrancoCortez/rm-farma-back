package owl.tree.rmfarma.product.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "code", unique = true, nullable = false, length = 30) private String code;
    @Column(name = "description", nullable = false, length = 100) private String description;
    @Column(name = "laboratory", nullable = false, length = 50) private String laboratory;

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

}
