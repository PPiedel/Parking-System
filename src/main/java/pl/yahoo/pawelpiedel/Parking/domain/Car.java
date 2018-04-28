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

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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
                Objects.equals(registrationNumber, car.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, registrationNumber);
    }

    @Override
    public String toString() {
        return "Car{" +
                "Id=" + Id +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }
}
