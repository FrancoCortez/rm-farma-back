package owl.tree.rmfarma.doctor.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.doctor.infrastructure.entities.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Doctor findByRut(String rut);
}
