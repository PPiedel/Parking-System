package pl.yahoo.pawelpiedel.Parking.service.driver;

import org.springframework.beans.factory.annotation.Autowired;
import pl.yahoo.pawelpiedel.Parking.domain.Driver;
import pl.yahoo.pawelpiedel.Parking.repository.DriverRepository;

import java.util.Optional;

public class DriverServiceImpl implements DriverService {
    private DriverRepository driverRepository;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Optional<Driver> findById(Long id) {
        return driverRepository.findById(id);
    }
}
