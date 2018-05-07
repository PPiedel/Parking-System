package pl.yahoo.pawelpiedel.Parking.service.parking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.repository.ParkingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ParkingServiceImpl implements ParkingService {
    private final ParkingRepository parkingRepository;

    @Autowired
    public ParkingServiceImpl(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Override
    public boolean isCarAlreadyParked(String licensePlateNumber) {
        return parkingRepository.findByCarLicensePlateNumberAndParkingStatus(licensePlateNumber, ParkingStatus.ONGOING).size() > 0;
    }

    @Override
    public Parking save(Parking entity) {
        return parkingRepository.save(entity);
    }

    @Override
    public Optional<Parking> findParkingById(Long id) {
        return parkingRepository.findById(id);
    }

    @Override
    public Parking stopParkingAtTime(Parking parking, LocalDateTime stopTime) {
        parking.setStopTime(stopTime);
        parking.setParkingStatus(ParkingStatus.COMPLETED);
        return parking;
    }
}
