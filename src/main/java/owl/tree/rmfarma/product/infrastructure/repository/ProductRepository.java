package owl.tree.rmfarma.product.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.product.infrastructure.entities.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByCode(String code);
}
