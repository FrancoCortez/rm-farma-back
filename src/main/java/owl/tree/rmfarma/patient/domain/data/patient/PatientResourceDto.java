package owl.tree.rmfarma.patient.domain.data.patient;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import owl.tree.rmfarma.country.domain.data.city.CityResourceDto;
import owl.tree.rmfarma.country.infrastructure.entities.City;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;
import owl.tree.rmfarma.domain.domain.data.schema.SchemaResourceDto;
import owl.tree.rmfarma.domain.infrastructure.entities.Clinic;
import owl.tree.rmfarma.domain.infrastructure.entities.Diagnosis;
import owl.tree.rmfarma.domain.infrastructure.entities.Isapre;
import owl.tree.rmfarma.domain.infrastructure.entities.Schema;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PatientResourceDto {
    private String id;
    private String identification;
    private String rut;
    private String name;
    private String lastName;
    private String villa;
    private String street;
    private String houseNumber;
    private OffsetDateTime dateOfBirth;
    private String phone;
    private String email;
    private Integer cycleNumber;
    private Integer cycleDay;
    private CityResourceDto city;
    private DoctorResourceDto doctor;
    private ServiceResourceDto services;
    private DiagnosisResourceDto diagnosis;
    private ClinicResourceDto clinic;
    private IsapreResourceDto isapre;
    private SchemaResourceDto schema;
}
