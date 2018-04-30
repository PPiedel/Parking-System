package pl.yahoo.pawelpiedel.Parking.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ParkingStopTimeDTO {
    @NotNull
    @NotEmpty
    private String stopTime;

    public ParkingStopTimeDTO() {
    }

    public ParkingStopTimeDTO(@NotNull @NotEmpty String stopTime) {
        this.stopTime = stopTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }
}
