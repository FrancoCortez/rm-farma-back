package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import owl.tree.rmfarma.manufacture.infrastructure.entities.CommercialOrderDetail;

@Repository
public interface CommercialOrderDetailRepository extends JpaRepository<CommercialOrderDetail, String> {

    @Modifying
    @Query(value = """
            delete from commercial_order_detail where order_detail_id = :orderDetail
            """, nativeQuery = true)
    void deleteByOrderDetailId(String orderDetail);


}
