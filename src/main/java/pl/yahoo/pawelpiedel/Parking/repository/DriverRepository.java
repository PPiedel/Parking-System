package pl.yahoo.pawelpiedel.Parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.yahoo.pawelpiedel.Parking.domain.Driver;

public interface DriverRepository extends JpaRepository<Driver,Long> {
}
