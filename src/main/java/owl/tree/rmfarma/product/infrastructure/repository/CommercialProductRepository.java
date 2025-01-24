package owl.tree.rmfarma.product.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.product.infrastructure.entities.CommercialProduct;

import java.util.Optional;

@Repository
public interface CommercialProductRepository extends JpaRepository<CommercialProduct, String> {
    Optional<CommercialProduct> findByCode(String code);
}
