package owl.tree.rmfarma.manufacture.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import owl.tree.rmfarma.domain.infrastructure.entities.DocumentType;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;

import java.time.OffsetDateTime;

@Entity(name = "master_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "master_record", unique = true, nullable = false, length = 30) private String masterRecord;
    @Column(name = "production_date", nullable = false) private OffsetDateTime productionDate;
    @Column(name = "patent_name", nullable = false, length = 50) private String patentName;
    @Column(name = "patent_rut", nullable = false, length = 12) private String patentRut;
    @Column(name = "doctor_name", nullable = false, length = 50) private String doctorName;
    @Column(name = "doctor_rut", nullable = false, length = 12) private String doctorRut;
    @Column(name = "clinic_name", nullable = false, length = 50) private String clinicName;
    @Column(name = "isapre_code", nullable = false, length = 10) private String isapreCode;
    @Column(name = "isapre_name", nullable = false, length = 100) private String isapreName;
    @Column(name = "diagnosis_code", nullable = false, length = 10) private String diagnosisCode;
    @Column(name = "diagnosis_name", nullable = false, length = 100) private String diagnosisName;
    @Column(name = "cycle_number", nullable = false) private Integer cycleNumber;
    @Column(name = "cycle_day", nullable = false) private Integer cycleDay;
    @Column(name = "schema_code", nullable = false, length = 30) private String schemaCode;
    @Column(name = "schema_name", nullable = false, length = 50) private String schemaName;
    @Column(name = "volume", nullable = false) private Double volume;
    @Column(name = "via_code", nullable = false, length = 30) private String viaCode;
    @Column(name = "via_description", nullable = false, length = 100) private String viaDescription;
    @Column(name = "pharmaceutical_chemist", nullable = false, length = 100) private String pharmaceuticalChemist;
    @Column(name = "document_type_code", nullable = false, length = 30) private String documentTypeCode;
    @Column(name = "document_type_name", nullable = false, length = 50) private String documentTypeName;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "via_id")
    private Via via;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

}
