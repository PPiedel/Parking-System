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
    public Place findPlaceForParking() {
        Place place = getFirstFreePlace();
        place.setPlaceStatus(PlaceStatus.TAKEN);
        return place;
    }

    private Place getFirstFreePlace() {
        List<Place> freePlaces = getAvailablePlaces();
        return freePlaces.get(0);
    }

    @Override
    public Place save(Place place) {
        return placeRepository.save(place);
    }

    @Override
    public Place releasePlace(Place place) {
        place.setPlaceStatus(PlaceStatus.AVAILABLE);
        return save(place);
    }
}
