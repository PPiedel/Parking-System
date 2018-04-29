package pl.yahoo.pawelpiedel.Parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.yahoo.pawelpiedel.Parking.domain.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByLicensePlateNumber(String number);
}
