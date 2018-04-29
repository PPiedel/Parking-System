package pl.yahoo.pawelpiedel.Parking.service.parking;

import org.springframework.stereotype.Service;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;

import java.util.List;

@Service
public interface ParkingService {
    List<Parking> getOngoingParkings(Car car);
}
