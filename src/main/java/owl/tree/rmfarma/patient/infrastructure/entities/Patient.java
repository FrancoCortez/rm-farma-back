package owl.tree.rmfarma.patient.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import owl.tree.rmfarma.country.infrastructure.entities.City;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;
import owl.tree.rmfarma.domain.infrastructure.entities.Clinic;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;
import owl.tree.rmfarma.domain.infrastructure.entities.Schema;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "patient")
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36) private String id;
    @Column(name = "rut", unique = true, nullable = false, length = 30) private String rut;
    @Column(name = "identification", unique = true, nullable = false, length = 30) private String identification;
    @Column(name = "name", nullable = false, length = 50) private String name;
    @Column(name = "last_name", nullable = false, length = 50) private String lastName;
    @Column(name = "villa", length = 100) private String villa;
    @Column(name = "street", length = 100) private String street;
    @Column(name = "house_number", length = 10) private String houseNumber;
    @Column(name = "date_of_birth") private OffsetDateTime dateOfBirth;
    @Column(name = "phone", length = 20) private String phone;
    @Column(name = "email", length = 100) private String email;
    @Column(name = "cycle_number", nullable = false) private Integer cycleNumber;
    @Column(name = "cycle_day", nullable = false) private Integer cycleDay;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

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
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "isapre_id")
    private Isapre isapre;

    @ManyToOne
    @JoinColumn(name = "schema_id")
    private Schema schema;

    @OneToMany(mappedBy = "patient", orphanRemoval = true)
    private Set<MasterOrder> masterOrders = new LinkedHashSet<>();

}
