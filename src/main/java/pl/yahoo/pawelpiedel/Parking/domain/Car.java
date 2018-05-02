package pl.yahoo.pawelpiedel.Parking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue
    private Long Id;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Parking> parkings;

    @ManyToOne()
    @JoinColumn(name = "driver_id")
    @JsonManagedReference
    @NotNull
    private Driver driver;

    @Column(name = "license_plate_number", unique = true)
    @NotNull
    private String licensePlateNumber;

    public Car() { //required by Hibernate
    }

    public Car(Driver driver, String licensePlateNumber) {
        this.driver = driver;
        this.licensePlateNumber = licensePlateNumber;
    }

    public void addparking(Parking parking) {
        if (parkings == null) {
            parkings = new ArrayList<>();
        }
        parkings.add(parking);
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public List<Parking> getParkings() {
        return parkings;
    }

    public void setParkings(List<Parking> parkings) {
        this.parkings = parkings;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(Id, car.Id) &&
                Objects.equals(licensePlateNumber, car.licensePlateNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, licensePlateNumber);
    }

    @Override
    public String toString() {
        return "Car{" +
                "Id=" + Id +
                ", licensePlateNumber='" + licensePlateNumber + '\'' +
                '}';
    }
}
