package pl.yahoo.pawelpiedel.Parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.yahoo.pawelpiedel.Parking.domain.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
}
