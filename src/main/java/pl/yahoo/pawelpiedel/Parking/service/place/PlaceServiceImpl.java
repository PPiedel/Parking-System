package pl.yahoo.pawelpiedel.Parking.service.place;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.repository.PlaceRepository;

@Component
public final class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public boolean isPlaceAvailable() {
        return !placeRepository.findByPlaceStatusIs(PlaceStatus.AVAILABLE).isEmpty();
    }
}
