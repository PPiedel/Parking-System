package pl.yahoo.pawelpiedel.Parking.service.driver;

import org.springframework.stereotype.Service;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;

import java.util.Optional;

@Service
public interface DriverService {
    Optional<Driver> findById(Long id);

    Driver save(Driver driver);

    Driver createNewDriver(DriverType driverType);
}
