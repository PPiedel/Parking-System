package pl.yahoo.pawelpiedel.Parking.dto;

import pl.yahoo.pawelpiedel.Parking.domain.date.LocalDateTimeConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ParkingStopTimeOnlyDTO {
    @NotNull
    @NotEmpty
    @LocalDateTimeConstraint
    private String stopTime;

    public ParkingStopTimeOnlyDTO() {
    }

    public ParkingStopTimeOnlyDTO(@NotNull @NotEmpty String stopTime) {
        this.stopTime = stopTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }
}
