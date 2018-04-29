package pl.yahoo.pawelpiedel.Parking.service.car;

import org.springframework.stereotype.Service;
import pl.yahoo.pawelpiedel.Parking.domain.Car;

@Service
public interface CarService {
    Car findByLicenceNumber(String licenceNumber);
}
