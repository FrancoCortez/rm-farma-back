package owl.tree.rmfarma.manufacture.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.domain.infrastructure.entities.DocumentType;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;
import owl.tree.rmfarma.shared.enumes.StateMachineEnum;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "master_order")
@Table(indexes = {
        @Index(name = "idx_status_master_order", columnList = "status"),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "master_record", unique = true, length = 30)
    private String masterRecord;
    @Column(name = "production_date")
    private OffsetDateTime productionDate;
    @Column(name = "patient_name", nullable = false, length = 50)
    private String patientName;
    @Column(name = "patient_last_name", nullable = false, length = 50)
    private String patientLastName;
    @Column(name = "patient_rut", nullable = false, length = 30)
    private String patientRut;
    @Column(name = "patient_identification", nullable = false, length = 30)
    private String patientIdentification;
    @Column(name = "doctor_name",  length = 50)
    private String doctorName;
    @Column(name = "doctor_rut", length = 30)
    private String doctorRut;
    @Column(name = "unit_hospital_code", length = 30)
    private String unitHospitalCode;
    @Column(name = "unit_name_name", length = 50)
    private String unitHospitalName;
    @Column(name = "isapre_code", length = 10)
    private Integer isapreCode;
    @Column(name = "isapre_name", length = 100)
    private String isapreName;
    @Column(name = "diagnosis_code",  length = 10)
    private String diagnosisCode;
    @Column(name = "diagnosis_name",  length = 300)
    private String diagnosisName;
    @Column(name = "cycle_number" )
    private Integer cycleNumber;
    @Column(name = "cycle_day")
    private Integer cycleDay;
    @Column(name = "schema_code",  length = 30)
    private String schemaCode;
    @Column(name = "schema_name", length = 50)
    private String schemaName;

    @Column(name = "document_type_code", length = 30)
    private String documentTypeCode;
    @Column(name = "document_type_name", length = 50)
    private String documentTypeName;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('CHECK_PATIENT_OK', 'FORMULATION_PREPARE', 'FORMULATION_OK') DEFAULT 'CHECK_PATIENT_OK'", nullable = false)
    private StateMachineEnum status;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    @OneToMany(mappedBy = "masterOrder", orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "diagnosis_order_stage_id")
    private DiagnosisOrderStage diagnosisOrderStage;

}
