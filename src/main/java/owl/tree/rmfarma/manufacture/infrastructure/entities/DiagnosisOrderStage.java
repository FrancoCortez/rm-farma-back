package owl.tree.rmfarma.manufacture.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.patient.infrastructure.entities.DiagnosisPatient;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;
import owl.tree.rmfarma.shared.enumes.StateMachineEnum;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "diagnosis_order_stage")
@Table(name = "diagnosis_order_stage",
        indexes = {
                @Index(name = "idx_status", columnList = "status"),
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisOrderStage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "identification_patient", nullable = false, length = 30)
    private String identificationPatient;
    @Column(name = "cycle_number", nullable = false)
    private Integer cycleNumber;
    @Column(name = "cycle_day", nullable = false)
    private Integer cycleDay;
    @Column(name = "production_date")
    private OffsetDateTime productionDate;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('CHECK_PATIENT_OK', 'FORMULATION_PREPARE', 'FORMULATION_OK') DEFAULT 'CHECK_PATIENT_OK'", nullable = false)
    private StateMachineEnum status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diagnosis_patient_id")
    private DiagnosisPatient diagnosisPatient;

    @OneToMany(mappedBy = "diagnosisOrderStage")
    private Set<MasterOrder> masterOrders = new LinkedHashSet<>();

}
