package owl.tree.rmfarma.patient.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import owl.tree.rmfarma.patient.infrastructure.entities.DiagnosisPatient;

@Repository
public interface DiagnosisPatientRepository extends JpaRepository<DiagnosisPatient, String> {

    @Modifying
    @Transactional
    @Query("UPDATE diagnosis_patient dp SET dp.cycleNumber = :cycleNumber, dp.cycleDay = :cycleDay WHERE dp.id = :id")
    void updateCycles(String id, Integer cycleNumber, Integer cycleDay);
}
