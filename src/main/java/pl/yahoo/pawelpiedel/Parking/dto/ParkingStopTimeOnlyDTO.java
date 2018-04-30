package pl.yahoo.pawelpiedel.Parking.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ParkingStopTimeOnlyDTO {
    @NotNull
    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
