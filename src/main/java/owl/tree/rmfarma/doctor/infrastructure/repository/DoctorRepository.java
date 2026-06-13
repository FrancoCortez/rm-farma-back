package owl.tree.rmfarma.doctor.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Optional<Doctor> findByRut(String rut);

    Optional<Doctor> findTopByOrderByCodeDesc();

    List<Doctor> findAllByEnabledTrue();

    Optional<Doctor> findByIdAndEnabledTrue(String id);

    Optional<Doctor> findByRutAndEnabledTrue(String rut);
}
