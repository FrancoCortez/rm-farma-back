package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.HospitalUnit;

import java.util.Optional;

@Repository
public interface HospitalUnitRepository extends JpaRepository<HospitalUnit, String> {
    Optional<HospitalUnit> findByCode(String code);
}
