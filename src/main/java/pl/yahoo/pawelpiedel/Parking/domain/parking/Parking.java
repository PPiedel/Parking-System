package pl.yahoo.pawelpiedel.Parking.domain.parking;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Payment;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "parking")
public class Parking {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonManagedReference
    @NotNull
    private Car car;

    @OneToOne
    @NotNull
    private Place place;

    @OneToOne(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @Column(name = "start_time")
    @NotNull
    private LocalDateTime startTime;

    @Column(name = "stop_time")
    private LocalDateTime stopTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "driver_type")
    @NotNull
    private ParkingStatus parkingStatus;

    public Parking() {
    }

    public Parking(@NotNull Car car, @NotNull Place place) {
        this.car = car;
        this.place = place;
        startTime = LocalDateTime.now();
        parkingStatus = ParkingStatus.ONGOING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime) {
        this.stopTime = stopTime;
    }

    public ParkingStatus getParkingStatus() {
        return parkingStatus;
    }

    public void setParkingStatus(ParkingStatus parkingStatus) {
        this.parkingStatus = parkingStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parking parking = (Parking) o;
        return Objects.equals(id, parking.id) &&
                Objects.equals(car, parking.car) &&
                Objects.equals(place, parking.place) &&
                Objects.equals(payment, parking.payment) &&
                Objects.equals(startTime, parking.startTime) &&
                Objects.equals(stopTime, parking.stopTime) &&
                parkingStatus == parking.parkingStatus;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, car, place, payment, startTime, stopTime, parkingStatus);
    }

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + id +
                ", car=" + car +
                ", place=" + place +
                ", payment=" + payment +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", parkingStatus=" + parkingStatus +
                '}';
    }
}
