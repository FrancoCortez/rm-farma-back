package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface MasterOrderRepository extends JpaRepository<MasterOrder, String> {

    List<MasterOrder> findAllByPatientIdentificationAndProductionDateBetween(
            String patientIdentification, OffsetDateTime productionDate, OffsetDateTime productionDate2);

    List<MasterOrder> findAllByProductionDateBetween(OffsetDateTime productionDate, OffsetDateTime productionDate2);
}
