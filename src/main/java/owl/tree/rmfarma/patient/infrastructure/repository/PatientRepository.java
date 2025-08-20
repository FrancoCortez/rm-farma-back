package owl.tree.rmfarma.patient.infrastructure.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.patient.domain.data.patient.PatientComboResourceDto;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    //    @Query("""
//                SELECT p FROM patient p
//                LEFT JOIN FETCH p.doctor d
//                LEFT JOIN FETCH p.isapre i
//                LEFT JOIN FETCH p.schema sh
//                LEFT JOIN FETCH p.clinic cl
//                LEFT JOIN FETCH p.diagnosis di
//                LEFT JOIN FETCH p.services s
//                LEFT JOIN FETCH p.hospitalUnit hu
//                WHERE p.identification = :identification
//            """)
    Patient findByIdentification(String identification);

    @EntityGraph(attributePaths = {"diagnosisPatients"})
    List<Patient> findAll();

    @Query("""
                SELECT new owl.tree.rmfarma.patient.domain.data.patient.PatientComboResourceDto(
                p.identification, concat(p.rut, ' || ', p.name, ' ', p.lastName)
            )
                        FROM patient p
                        WHERE (:identification IS NULL OR :identification = '' OR p.identification LIKE %:identification%)
            """)
    List<PatientComboResourceDto> findPatientByIdentificationContaining(@Param("identification") String identification);
}
