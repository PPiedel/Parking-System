package pl.yahoo.pawelpiedel.Parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
    public List<Parking> findByCarLicensePlateNumberAndParkingStatus(String licensePlateNumber, ParkingStatus parkingStatus);
}
