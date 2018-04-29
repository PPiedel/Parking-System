package pl.yahoo.pawelpiedel.Parking.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue
    private Long Id;

    @OneToMany(mappedBy = "car")
    private List<Parking> parkings;

    @ManyToOne()
    @JoinColumn(name = "driver_id")
    @JsonManagedReference
    private Driver driver;

    @Column(name = "license_plate_number", unique = true)
    private String licensePlateNumber;

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