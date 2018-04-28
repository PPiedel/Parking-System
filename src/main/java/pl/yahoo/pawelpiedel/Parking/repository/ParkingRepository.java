package pl.yahoo.pawelpiedel.Parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.yahoo.pawelpiedel.Parking.domain.Parking;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
}
