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
    public Parking save(LocalDateTime stopTime, Long id) throws ParkingNotFoundException {
        Optional<Parking> parkingOptional = parkingRepository.findById(id);
        if (parkingOptional.isPresent()) {
            Parking entity = parkingOptional.get();
            entity.setStopTime(stopTime);
            return entity;
        } else {
            throw new ParkingNotFoundException("Parking with id " + id + " not found.");
        }

    }
}
