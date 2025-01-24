package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
