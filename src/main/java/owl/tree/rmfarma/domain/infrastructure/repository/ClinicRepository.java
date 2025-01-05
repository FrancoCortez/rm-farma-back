package owl.tree.rmfarma.domain.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.domain.infrastructure.entities.Clinic;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, String> {

    Optional<Clinic> findByCode(String code);
}
