package owl.tree.rmfarma.country.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.country.infrastructure.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, String> {
}
