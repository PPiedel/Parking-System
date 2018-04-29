package pl.yahoo.pawelpiedel.Parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByPlaceStatusIs(PlaceStatus placeStatus);
}
