package pl.yahoo.pawelpiedel.Parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {
}
