package pl.yahoo.pawelpiedel.Parking.service.driver;

import pl.yahoo.pawelpiedel.Parking.domain.Driver;

import java.util.Optional;

public interface DriverService {
    Optional<Driver> findById(Long id);
}
