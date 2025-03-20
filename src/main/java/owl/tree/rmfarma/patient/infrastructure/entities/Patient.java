package owl.tree.rmfarma.patient.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;
import owl.tree.rmfarma.manufacture.infrastructure.entities.DiagnosisOrderStage;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "patient")
@Table(name = "patient", indexes = {
        @Index(name = "idx_patient_rut", columnList = "rut"),
        @Index(name = "idx_patient_identification", columnList = "identification"),
        @Index(name = "idx_patient_type", columnList = "type"),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "rut", nullable = false, length = 30)
    private String rut;
    @Column(name = "identification", nullable = false, length = 30)
    private String identification;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    @OneToMany(mappedBy = "patient")
    private Set<MasterOrder> masterOrders = new LinkedHashSet<>();
    @ManyToOne
    @JoinColumn(name = "isapre_id")
    private Isapre isapre;
    @OneToMany(mappedBy = "patient")
    private Set<DiagnosisPatient> diagnosisPatients = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<DiagnosisOrderStage> diagnosisOrderStages = new LinkedHashSet<>();

}
