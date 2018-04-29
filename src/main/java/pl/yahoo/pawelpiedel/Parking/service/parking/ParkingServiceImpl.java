package pl.yahoo.pawelpiedel.Parking.service.parking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.repository.ParkingRepository;

import java.util.List;

@Component
public class ParkingServiceImpl implements ParkingService {
    private final ParkingRepository parkingRepository;

    @Autowired
    public ParkingServiceImpl(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Override
    public List<Parking> getOngoingParkings(Car car) {
        return parkingRepository.findByCarAndParkingStatus(car, ParkingStatus.ONGOING);
    }
}
