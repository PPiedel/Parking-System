package pl.yahoo.pawelpiedel.Parking.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Entity
@Table(name = "driver")
public class Driver {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "driver_type")
    private DriverType driverType;

    @OneToMany(mappedBy = "driver")
    private List<Parking> parkings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }

    public List<Parking> getParkings() {
        return parkings;
    }

    public void setParkings(List<Parking> parkings) {
        this.parkings = parkings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return driverType == driver.driverType &&
                Objects.equals(parkings, driver.parkings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverType, parkings);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverType=" + driverType +
                ", parkings=" + parkings.stream().map(Object::toString).collect(Collectors.toList()) +
                '}';
    }
}
