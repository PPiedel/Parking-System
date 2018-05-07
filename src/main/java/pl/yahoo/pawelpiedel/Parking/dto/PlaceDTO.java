package pl.yahoo.pawelpiedel.Parking.dto;

import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;

public class PlaceDTO {
    private Long id;

    private PlaceStatus placeStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlaceStatus getPlaceStatus() {
        return placeStatus;
    }

    public void setPlaceStatus(PlaceStatus placeStatus) {
        this.placeStatus = placeStatus;
    }
}
