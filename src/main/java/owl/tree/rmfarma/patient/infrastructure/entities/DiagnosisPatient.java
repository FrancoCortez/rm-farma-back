package owl.tree.rmfarma.patient.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;
import owl.tree.rmfarma.domain.infrastructure.entities.HospitalUnit;
import owl.tree.rmfarma.domain.infrastructure.entities.Schema;
import owl.tree.rmfarma.manufacture.infrastructure.entities.DiagnosisOrderStage;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "diagnosis_patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisPatient {

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

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "services_id")
    private Services services;

    @ManyToOne
    @JoinColumn(name = "diagnosis_id")
    private Diagnosis diagnosis;

    @ManyToOne
    @JoinColumn(name = "schema_id")
    private Schema schema;

    @ManyToOne
    @JoinColumn(name = "hospital_unit_id")
    private HospitalUnit hospitalUnit;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "diagnosisPatient")
    private Set<DiagnosisOrderStage> diagnosisOrderStages = new LinkedHashSet<>();

}
