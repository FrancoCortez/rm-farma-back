package owl.tree.rmfarma.patient.infrastructure.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.patient.infrastructure.entities.Patient;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    @Query("""
    SELECT p FROM patient p
    LEFT JOIN FETCH p.doctor d
    LEFT JOIN FETCH p.schema sh
    LEFT JOIN FETCH p.city c
    LEFT JOIN FETCH p.clinic cl
    LEFT JOIN FETCH p.isapre i
    LEFT JOIN FETCH p.diagnosis di
    LEFT JOIN FETCH p.services s
    WHERE p.identification = :identification
""")
    Patient findByIdentification(@Param("identification") String identification);
}
