package pl.yahoo.pawelpiedel.Parking.service.place;

import org.springframework.stereotype.Service;

@Service
public interface PlaceService {
    boolean isPlaceAvailable();
}
