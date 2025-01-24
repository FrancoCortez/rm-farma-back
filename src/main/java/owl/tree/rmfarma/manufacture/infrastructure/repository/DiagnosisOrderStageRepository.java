package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.manufacture.infrastructure.entities.DiagnosisOrderStage;

import java.util.Optional;

@Repository
public interface DiagnosisOrderStageRepository extends JpaRepository<DiagnosisOrderStage, String> {

    @Query(""" 
            SELECT d FROM diagnosis_order_stage d
                        LEFT JOIN FETCH d.patient
                        LEFT JOIN FETCH d.diagnosisPatient
                                    WHERE d.id = ?1
            """)
    Optional<DiagnosisOrderStage> findByIdWithPatientAndDiagnosisPatient(String id);
}
