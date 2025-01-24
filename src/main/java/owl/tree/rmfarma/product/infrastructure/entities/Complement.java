package owl.tree.rmfarma.product.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "complement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "code", unique = true, nullable = false, length = 30)
    private String code;
    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @OneToMany(mappedBy = "complement", orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

}
