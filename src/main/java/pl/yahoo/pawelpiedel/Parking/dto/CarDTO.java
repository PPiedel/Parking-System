package pl.yahoo.pawelpiedel.Parking.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CarDTO {
    @NotNull
    @NotEmpty
    private String licensePlateNumber;

    public CarDTO() {
    }

    public CarDTO(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    @Override
    public String toString() {
        return "CarDTO{" +
                "licensePlateNumber='" + licensePlateNumber + '\'' +
                '}';
    }
}
