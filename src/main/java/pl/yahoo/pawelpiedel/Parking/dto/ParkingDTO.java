package pl.yahoo.pawelpiedel.Parking.dto;

import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;

import java.time.LocalDateTime;

public class ParkingDTO {

    private CarDTO car;

    private PlaceDTO place;

    private LocalDateTime startTime;

    private LocalDateTime stopTime;

    private ParkingStatus parkingStatus;

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public PlaceDTO getPlace() {
        return place;
    }

    public void setPlace(PlaceDTO place) {
        this.place = place;
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
}
