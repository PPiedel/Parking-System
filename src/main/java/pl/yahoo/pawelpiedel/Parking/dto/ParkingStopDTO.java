package pl.yahoo.pawelpiedel.Parking.dto;

import pl.yahoo.pawelpiedel.Parking.domain.date.LocalDateTimeConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ParkingStopDTO {
    @NotNull
    @NotEmpty
    @LocalDateTimeConstraint
    private String stopTime;

    @NotNull
    @NotEmpty
    private String currencyType;

    public ParkingStopDTO() {
    }

    public ParkingStopDTO(@NotNull @NotEmpty String stopTime, @NotNull @NotEmpty String currencyType) {
        this.stopTime = stopTime;
        this.currencyType = currencyType;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
