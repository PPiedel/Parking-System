package pl.yahoo.pawelpiedel.Parking.service.parking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.yahoo.pawelpiedel.Parking.repository.ParkingRepository;

@Component
public class ParkingServiceImpl implements ParkingService {
    private final ParkingRepository parkingRepository;

    @Autowired
    public ParkingServiceImpl(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }
}
