package pl.yahoo.pawelpiedel.Parking.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public final class ParkingDTO {
    @NotNull
    private final CarDTO car;
    private final LocalDateTime startTime;

    public ParkingDTO(@NotNull CarDTO car, LocalDateTime startTime) {
        this.car = car;
        this.startTime = startTime;
    }

    public CarDTO getCar() {
        return car;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "ParkingDTO{" +
                "car=" + car +
                ", startTime=" + startTime +
                '}';
    }
}
