package pl.yahoo.pawelpiedel.Parking.domain.place;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Place {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "place_status")
    private PlaceStatus placeStatus;

    public Place() {//required by Hibernate
    }

    public Place(PlaceStatus placeStatus) {
        this.placeStatus = placeStatus;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(id, place.id) &&
                placeStatus == place.placeStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placeStatus);
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", placeStatus=" + placeStatus +
                '}';
    }
}
