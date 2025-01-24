package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.manufacture.infrastructure.entities.CommercialOrderDetail;

@Repository
public interface CommercialOrderDetailRepository extends JpaRepository<CommercialOrderDetail, String> {
}
