package pl.yahoo.pawelpiedel.Parking.service.place;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.repository.PlaceRepository;

import java.util.List;

@Component
public final class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }


    @Override
    public List<Place> getAvailablePlaces() {
        return placeRepository.findByPlaceStatusIs(PlaceStatus.AVAILABLE);
    }

    @Override
    public Place getNextFirstFreePlace() {
        List<Place> freePlaces = getAvailablePlaces();
        return freePlaces.get(0);
    }

    @Override
    public Place save(Place place) {
        return placeRepository.save(place);
    }
}
