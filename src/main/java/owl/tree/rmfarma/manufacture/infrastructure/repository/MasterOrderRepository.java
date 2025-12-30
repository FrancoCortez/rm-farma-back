package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface MasterOrderRepository extends JpaRepository<MasterOrder, String> {

    @Query("""
                SELECT m
                FROM master_order m
                WHERE (
                    (:identification IS NULL OR :identification = '' OR m.patientIdentification LIKE %:identification%)
                    OR (:identification IS NULL OR :identification = '' OR m.patientName LIKE %:identification%)
                    OR (:identification IS NULL OR :identification = '' OR m.patientRut LIKE %:identification%)
                    OR (:identification IS NULL OR :identification = '' OR m.patientLastName LIKE %:identification%)
                )
                AND m.productionDate BETWEEN :startDate AND :endDate
                            order by m.productionDate desc
            """)
    List<MasterOrder> findAllByPatientIdentificationAndProductionDateBetween(
            String identification, OffsetDateTime startDate, OffsetDateTime endDate);

    List<MasterOrder> findAllByProductionDateBetween(OffsetDateTime productionDate, OffsetDateTime productionDate2);

    @Query("""
                SELECT DISTINCT m
                FROM master_order m
                JOIN m.orderDetails od
                WHERE m.patientIdentification = :identification
                  AND od.masterRecord IS NOT NULL
                  AND m.diagnosisOrderStage.diagnosisPatient.id = :diagnosisId
                ORDER BY m.cycleNumber DESC
            """)
    List<MasterOrder> findTop10ByPatientIdentificationWithOrderDetailRMNotNull(
            @Param("identification") String identification,
            @Param("diagnosisId") String diagnosisId,
            Pageable pageable
    );
}
