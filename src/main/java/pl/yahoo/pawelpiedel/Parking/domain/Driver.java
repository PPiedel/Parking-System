package pl.yahoo.pawelpiedel.Parking.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    @JsonBackReference
    private List<Car> cars;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id) &&
                driverType == driver.driverType &&
                Objects.equals(cars, driver.cars);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, driverType, cars);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", driverType=" + driverType +
                ", cars=" + cars.stream().map(Car::toString).collect(Collectors.toList()) +
                '}';
    }
}
