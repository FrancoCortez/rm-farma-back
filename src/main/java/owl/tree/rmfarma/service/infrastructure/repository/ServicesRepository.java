package owl.tree.rmfarma.service.infrastructure.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, String> {

    Optional<Services> findByCode(String code);
}
