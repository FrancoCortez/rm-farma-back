package owl.tree.rmfarma.domain.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "document_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;
    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @OneToMany(mappedBy = "documentType")
    private Set<MasterOrder> masterOrders = new LinkedHashSet<>();

}
